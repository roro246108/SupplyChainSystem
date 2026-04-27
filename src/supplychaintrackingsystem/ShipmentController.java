package supplychaintrackingsystem;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipmentController {

    private static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final Map<Integer, Shipment> shipments = new HashMap<>();

    public ShipmentController() {
    }

    public Shipment createShipment(int id, String origin, String destination, double weight, double distance) {
        try {
            Shipment shipment = new Shipment(id, origin, destination, weight, distance);
            shipments.put(id, shipment);

            System.out.println("Shipment created successfully.");
            return shipment;
        } catch (Exception e) {
            System.out.println("Error creating shipment: " + e.getMessage());
            return null;
        }
    }

    public Shipment loadShipment(int shipmentID) {
        if (shipmentID <= 0) {
            throw new IllegalArgumentException("Shipment ID must be positive.");
        }

        try (Connection conn = DBConnection.connect()) {
            ensureShipmentSchema(conn);

            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT shipment_id, order_id, origin, destination, current_location, status, " +
                    "weight, distance, departure_date, arrival_date, temperature, humidity, condition_notes " +
                    "FROM shipments WHERE shipment_id = ?")) {
                ps.setInt(1, shipmentID);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalArgumentException("Shipment not found.");
                    }

                    Shipment shipment = mapShipment(rs);
                    shipments.put(shipment.getShipmentID(), shipment);
                    return shipment;
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load shipment: " + e.getMessage(), e);
        }
    }

    public Shipment loadShipmentByOrderId(int orderID) {
        if (orderID <= 0) {
            throw new IllegalArgumentException("Order ID must be positive.");
        }

        try (Connection conn = DBConnection.connect()) {
            ensureShipmentSchema(conn);

            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT shipment_id, order_id, origin, destination, current_location, status, " +
                    "weight, distance, departure_date, arrival_date, temperature, humidity, condition_notes " +
                    "FROM shipments WHERE order_id = ? ORDER BY shipment_id ASC LIMIT 1")) {
                ps.setInt(1, orderID);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalArgumentException("Shipment for order not found.");
                    }

                    Shipment shipment = mapShipment(rs);
                    shipments.put(shipment.getShipmentID(), shipment);
                    return shipment;
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to load shipment by order: " + e.getMessage(), e);
        }
    }

    public Shipment saveShipment(int orderID, Shipment shipment) {
        if (shipment == null) {
            throw new NullPointerException("Shipment cannot be null.");
        }

        int resolvedOrderId = orderID > 0 ? orderID : shipment.getOrderID();
        if (resolvedOrderId <= 0) {
            throw new IllegalArgumentException("Order ID must be positive.");
        }

        shipment.setOrderID(resolvedOrderId);

        try (Connection conn = DBConnection.connect()) {
            ensureShipmentSchema(conn);

            boolean exists = shipment.getShipmentID() > 0 && shipmentExists(conn, shipment.getShipmentID());
            if (exists) {
                updateShipmentRow(conn, shipment);
                appendNotification(shipment.getShipmentID(), "Shipment updated.");
                shipments.put(shipment.getShipmentID(), shipment);
                return loadShipment(shipment.getShipmentID());
            }

            int shipmentId = insertShipmentRow(conn, shipment);
            shipment.setShipmentID(shipmentId);
            appendNotification(shipmentId, "Shipment created.");
            shipments.put(shipmentId, shipment);
            return loadShipment(shipmentId);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save shipment: " + e.getMessage(), e);
        }
    }

    public void sendShipment(int shipmentID, int retailerID) {
        if (retailerID <= 0) {
            throw new IllegalArgumentException("Retailer ID must be positive.");
        }

        Shipment shipment = loadShipment(shipmentID);
        shipment.sendShipmentToRetailer(shipmentID, retailerID);
        shipment.setStatus("In Transit");
        saveShipment(shipment.getOrderID(), shipment);
        appendNotification(shipmentID, "Shipment sent to retailer ID: " + retailerID);
    }

    public void updateShipmentStatus(int shipmentID, String status) {
        Shipment shipment = loadShipment(shipmentID);
        shipment.setStatus(status);
        if ("Delivered".equalsIgnoreCase(shipment.getStatus())) {
            shipment.setArrivalDate(new java.util.Date());
        }
        saveShipment(shipment.getOrderID(), shipment);
        appendNotification(shipmentID, "Status updated to " + shipment.getStatus() + ".");
    }

    public void updateLocation(int shipmentID, String location) {
        Shipment shipment = loadShipment(shipmentID);
        shipment.updateLocation(location);
        saveShipment(shipment.getOrderID(), shipment);
        appendNotification(shipmentID, "Location updated to " + shipment.getCurrentLocation() + ".");
    }

    public void updateConditions(int shipmentID, double temp, double humidity) {
        Shipment shipment = loadShipment(shipmentID);
        shipment.updateConditions(temp, humidity);
        shipment.setTemperature(temp);
        shipment.setHumidity(humidity);
        saveShipment(shipment.getOrderID(), shipment);
        appendNotification(shipmentID, "Conditions updated: temperature=" + temp + ", humidity=" + humidity + ".");
    }

    public void updateConditions(int shipmentID, double temp, double humidity, String notes) {
        Shipment shipment = loadShipment(shipmentID);
        shipment.updateConditions(temp, humidity);
        shipment.setTemperature(temp);
        shipment.setHumidity(humidity);
        shipment.setConditionNotes(notes);
        saveShipment(shipment.getOrderID(), shipment);
        appendNotification(shipmentID, notes == null || notes.isBlank()
                ? "Condition monitoring updated."
                : notes.trim());
    }

    public void receiveShipment(int shipmentID) {
        Shipment shipment = loadShipment(shipmentID);
        shipment.receiveShipment(shipmentID);
        shipment.setStatus("Delivered");
        shipment.setArrivalDate(new java.util.Date());
        saveShipment(shipment.getOrderID(), shipment);
        appendNotification(shipmentID, "Shipment received.");
    }

    public void deliverShipment(int shipmentID) {
        Shipment shipment = loadShipment(shipmentID);
        shipment.markAsDelivered();
        saveShipment(shipment.getOrderID(), shipment);
        appendNotification(shipmentID, "Shipment delivered.");
    }

    public String trackShipment(int shipmentID) {
        Shipment shipment = loadShipment(shipmentID);
        StringBuilder summary = new StringBuilder();
        summary.append("Shipment ID: ").append(shipment.getShipmentID()).append('\n')
               .append("Order ID: ").append(shipment.getOrderID()).append('\n')
               .append("Origin: ").append(nullSafe(shipment.getOrigin())).append('\n')
               .append("Destination: ").append(nullSafe(shipment.getDestination())).append('\n')
               .append("Current Location: ").append(nullSafe(shipment.getCurrentLocation())).append('\n')
               .append("Status: ").append(nullSafe(shipment.getStatus())).append('\n')
               .append("Departure Date: ").append(formatDate(shipment.getDepartureDate())).append('\n')
               .append("Arrival Date: ").append(formatDate(shipment.getArrivalDate())).append('\n')
               .append("Weight: ").append(shipment.getWeight()).append('\n')
               .append("Distance: ").append(shipment.getDistance()).append('\n');

        List<String> history = loadNotifications(shipmentID);
        if (!history.isEmpty()) {
            summary.append("\nTracking Notes:\n");
            for (String note : history) {
                summary.append("- ").append(note).append('\n');
            }
        }

        return summary.toString();
    }

    public double calculateCost(int shipmentID) {
        Shipment shipment = loadShipment(shipmentID);
        return shipment.calculateShippingCost();
    }

    public String estimateTime(int shipmentID) {
        Shipment shipment = loadShipment(shipmentID);
        return shipment.estimateDeliveryTime();
    }

    public java.util.Date calculateEtaDate(int shipmentID) {
        Shipment shipment = loadShipment(shipmentID);
        java.util.Date eta = shipment.calculateETA();
        shipment.setArrivalDate(eta);
        saveShipment(shipment.getOrderID(), shipment);
        appendNotification(shipmentID, "ETA calculated for " + formatDate(eta) + ".");
        return eta;
    }

    public void markDelivered(int shipmentID) {
        Shipment shipment = loadShipment(shipmentID);
        shipment.markAsDelivered();
        shipment.setArrivalDate(new java.util.Date());
        saveShipment(shipment.getOrderID(), shipment);
        appendNotification(shipmentID, "Shipment marked as delivered.");
    }

    private int insertShipmentRow(Connection conn, Shipment shipment) throws SQLException {
        boolean includeId = shipment.getShipmentID() > 0;
        String sql = includeId
                ? "INSERT INTO shipments (shipment_id, order_id, origin, destination, current_location, status, weight, distance, departure_date, arrival_date, scheduled_delivery_date, receiver_name, delivery_location, delivery_status, delivery_issue, temperature, humidity, condition_notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                : "INSERT INTO shipments (order_id, origin, destination, current_location, status, weight, distance, departure_date, arrival_date, scheduled_delivery_date, receiver_name, delivery_location, delivery_status, delivery_issue, temperature, humidity, condition_notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            if (includeId) {
                ps.setInt(index++, shipment.getShipmentID());
            }
            bindShipment(ps, shipment, index);
            ps.executeUpdate();

            if (includeId) {
                return shipment.getShipmentID();
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            throw new SQLException("Shipment ID was not generated.");
        }
    }

    private void updateShipmentRow(Connection conn, Shipment shipment) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE shipments SET order_id = ?, origin = ?, destination = ?, current_location = ?, status = ?, " +
                "weight = ?, distance = ?, departure_date = ?, arrival_date = ?, scheduled_delivery_date = ?, receiver_name = ?, delivery_location = ?, delivery_status = ?, delivery_issue = ?, temperature = ?, humidity = ?, condition_notes = ? " +
                "WHERE shipment_id = ?")) {
            bindShipment(ps, shipment, 1);
            ps.setInt(18, shipment.getShipmentID());
            ps.executeUpdate();
        }
    }

    private void bindShipment(PreparedStatement ps, Shipment shipment, int startIndex) throws SQLException {
        int index = startIndex;
        ps.setInt(index++, shipment.getOrderID());
        ps.setString(index++, shipment.getOrigin());
        ps.setString(index++, shipment.getDestination());
        ps.setString(index++, shipment.getCurrentLocation());
        ps.setString(index++, toDbStatus(shipment.getStatus()));
        ps.setDouble(index++, shipment.getWeight());
        ps.setDouble(index++, shipment.getDistance());
        setTimestamp(ps, index++, shipment.getDepartureDate());
        setTimestamp(ps, index++, shipment.getArrivalDate());
        setTimestamp(ps, index++, shipment.getScheduledDeliveryDate());
        ps.setString(index++, shipment.getReceiverName());
        ps.setString(index++, shipment.getDeliveryLocation());
        ps.setString(index++, shipment.getDeliveryStatus());
        ps.setString(index++, shipment.getDeliveryIssue());
        setNullableDouble(ps, index++, shipment.getTemperature());
        setNullableDouble(ps, index++, shipment.getHumidity());
        ps.setString(index, shipment.getConditionNotes());
    }

    private Shipment mapShipment(ResultSet rs) throws SQLException {
        Shipment shipment = new Shipment();
        shipment.setShipmentID(rs.getInt("shipment_id"));
        shipment.setOrderID(rs.getInt("order_id"));
        shipment.setOrigin(nullSafe(rs.getString("origin"), "Unknown origin"));
        shipment.setDestination(nullSafe(rs.getString("destination"), "Unknown destination"));

        String currentLocation = rs.getString("current_location");
        shipment.setCurrentLocation(currentLocation == null || currentLocation.isBlank()
                ? shipment.getOrigin()
                : currentLocation);

        double weight = rs.getDouble("weight");
        if (!rs.wasNull() && weight > 0) {
            shipment.setWeight(weight);
        }

        double distance = rs.getDouble("distance");
        if (!rs.wasNull() && distance > 0) {
            shipment.setDistance(distance);
        }

        Timestamp departure = rs.getTimestamp("departure_date");
        if (departure != null) {
            shipment.setDepartureDate(new java.util.Date(departure.getTime()));
        }

        Timestamp arrival = rs.getTimestamp("arrival_date");
        if (arrival != null) {
            shipment.setArrivalDate(new java.util.Date(arrival.getTime()));
        }

        Timestamp scheduled = rs.getTimestamp("scheduled_delivery_date");
        if (scheduled != null) {
            shipment.setScheduledDeliveryDate(new java.util.Date(scheduled.getTime()));
        }

        String receiverName = rs.getString("receiver_name");
        if (receiverName != null && !receiverName.isBlank()) {
            shipment.setReceiverName(receiverName);
        }

        String deliveryLocation = rs.getString("delivery_location");
        if (deliveryLocation != null && !deliveryLocation.isBlank()) {
            shipment.setDeliveryLocation(deliveryLocation);
        }

        String deliveryStatus = rs.getString("delivery_status");
        if (deliveryStatus != null && !deliveryStatus.isBlank()) {
            shipment.setDeliveryStatus(deliveryStatus);
        }

        String deliveryIssue = rs.getString("delivery_issue");
        if (deliveryIssue != null && !deliveryIssue.isBlank()) {
            shipment.setDeliveryIssue(deliveryIssue);
        }

        double temperature = rs.getDouble("temperature");
        if (!rs.wasNull()) {
            shipment.setTemperature(temperature);
        }

        double humidity = rs.getDouble("humidity");
        if (!rs.wasNull()) {
            shipment.setHumidity(humidity);
        }

        String status = rs.getString("status");
        if (status != null && !status.isBlank()) {
            shipment.setStatus(fromDbStatus(status));
        }

        shipment.setConditionNotes(rs.getString("condition_notes"));
        return shipment;
    }

    private void appendNotification(int shipmentID, String message) {
        if (message == null || message.isBlank()) {
            return;
        }

        try (Connection conn = DBConnection.connect()) {
            ensureShipmentSchema(conn);
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO notifications (user_id, shipment_id, message, is_read) VALUES (?, ?, ?, 0)")) {
                ps.setInt(1, resolveUserId());
                ps.setInt(2, shipmentID);
                ps.setString(3, message.trim());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Notification save failed: " + e.getMessage());
        }
    }

    private List<String> loadNotifications(int shipmentID) {
        List<String> notes = new ArrayList<>();

        try (Connection conn = DBConnection.connect()) {
            ensureShipmentSchema(conn);
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT message FROM notifications WHERE shipment_id = ? ORDER BY created_at DESC, notification_id DESC")) {
                ps.setInt(1, shipmentID);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String message = rs.getString("message");
                        if (message != null && !message.isBlank()) {
                            notes.add(message.trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Could not load shipment tracking notes: " + e.getMessage());
        }

        return notes;
    }

    private void ensureShipmentSchema() {
        try (Connection conn = DBConnection.connect()) {
            ensureShipmentSchema(conn);
        } catch (Exception e) {
            System.out.println("Shipment schema check failed: " + e.getMessage());
        }
    }

    private void ensureShipmentSchema(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS shipments (
                        shipment_id INT NOT NULL AUTO_INCREMENT,
                        order_id INT NOT NULL,
                        origin VARCHAR(100) DEFAULT NULL,
                        destination VARCHAR(100) DEFAULT NULL,
                        current_location VARCHAR(100) DEFAULT NULL,
                        status VARCHAR(50) DEFAULT 'CREATED',
                        weight DECIMAL(10,2) DEFAULT NULL,
                        distance DECIMAL(10,2) DEFAULT NULL,
                        departure_date DATETIME DEFAULT NULL,
                        arrival_date DATETIME DEFAULT NULL,
                        scheduled_delivery_date DATETIME DEFAULT NULL,
                        receiver_name VARCHAR(150) DEFAULT NULL,
                        delivery_location VARCHAR(150) DEFAULT NULL,
                        delivery_status VARCHAR(50) DEFAULT NULL,
                        delivery_issue VARCHAR(255) DEFAULT NULL,
                        temperature DECIMAL(10,2) DEFAULT NULL,
                        humidity DECIMAL(10,2) DEFAULT NULL,
                        condition_notes VARCHAR(255) DEFAULT NULL,
                        PRIMARY KEY (shipment_id),
                        KEY order_id (order_id)
                    ) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
                    """);
        }

        addColumnIfMissing(conn, "shipments", "temperature", "ALTER TABLE shipments ADD COLUMN temperature DECIMAL(10,2) DEFAULT NULL AFTER distance");
        addColumnIfMissing(conn, "shipments", "humidity", "ALTER TABLE shipments ADD COLUMN humidity DECIMAL(10,2) DEFAULT NULL AFTER temperature");
        addColumnIfMissing(conn, "shipments", "scheduled_delivery_date", "ALTER TABLE shipments ADD COLUMN scheduled_delivery_date DATETIME DEFAULT NULL AFTER arrival_date");
        addColumnIfMissing(conn, "shipments", "receiver_name", "ALTER TABLE shipments ADD COLUMN receiver_name VARCHAR(150) DEFAULT NULL AFTER scheduled_delivery_date");
        addColumnIfMissing(conn, "shipments", "delivery_location", "ALTER TABLE shipments ADD COLUMN delivery_location VARCHAR(150) DEFAULT NULL AFTER receiver_name");
        addColumnIfMissing(conn, "shipments", "delivery_status", "ALTER TABLE shipments ADD COLUMN delivery_status VARCHAR(50) DEFAULT NULL AFTER delivery_location");
        addColumnIfMissing(conn, "shipments", "delivery_issue", "ALTER TABLE shipments ADD COLUMN delivery_issue VARCHAR(255) DEFAULT NULL AFTER delivery_status");
        addColumnIfMissing(conn, "shipments", "condition_notes", "ALTER TABLE shipments ADD COLUMN condition_notes VARCHAR(255) DEFAULT NULL AFTER humidity");
    }

    private void addColumnIfMissing(Connection conn, String tableName, String columnName, String alterSql) throws SQLException {
        if (!columnExists(conn, tableName, columnName)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(alterSql);
            }
        }
    }

    private boolean columnExists(Connection conn, String tableName, String columnName) throws SQLException {
        String databaseName = resolveDatabaseName(conn);

        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = ? AND table_name = ? AND column_name = ?")) {
            ps.setString(1, databaseName);
            ps.setString(2, tableName);
            ps.setString(3, columnName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    private String resolveDatabaseName(Connection conn) {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DATABASE()")) {
            if (rs.next()) {
                String databaseName = rs.getString(1);
                if (databaseName != null && !databaseName.isBlank()) {
                    return databaseName;
                }
            }
        } catch (Exception e) {
            System.out.println("Could not resolve database name: " + e.getMessage());
        }

        return "supply_chain";
    }

    private boolean shipmentExists(Connection conn, int shipmentID) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM shipments WHERE shipment_id = ?")) {
            ps.setInt(1, shipmentID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void setTimestamp(PreparedStatement ps, int index, java.util.Date value) throws SQLException {
        if (value == null) {
            ps.setTimestamp(index, null);
        } else {
            ps.setTimestamp(index, new Timestamp(value.getTime()));
        }
    }

    private void setNullableDouble(PreparedStatement ps, int index, double value) throws SQLException {
        if (Double.isNaN(value) || value <= 0) {
            ps.setNull(index, java.sql.Types.DECIMAL);
        } else {
            ps.setDouble(index, value);
        }
    }

    private int resolveUserId() {
        User currentUser = AppContext.getCurrentUser();
        if (currentUser != null && currentUser.getUserID() > 0) {
            return currentUser.getUserID();
        }
        return 1;
    }

    private String toDbStatus(String status) {
        if (status == null) {
            return "CREATED";
        }

        String normalized = status.trim().replace(' ', '_').toUpperCase();
        if ("PENDING".equals(normalized)) {
            return "CREATED";
        }
        return normalized;
    }

    private String fromDbStatus(String status) {
        if (status == null) {
            return "Created";
        }

        String normalized = status.trim().replace('_', ' ').toLowerCase();
        if ("created".equals(normalized)) {
            return "Created";
        }
        if ("in transit".equals(normalized)) {
            return "In Transit";
        }
        if ("out for delivery".equals(normalized)) {
            return "Out for Delivery";
        }
        if ("delivered".equals(normalized)) {
            return "Delivered";
        }
        if ("delayed".equals(normalized)) {
            return "Delayed";
        }
        if ("cancelled".equals(normalized)) {
            return "Cancelled";
        }
        return status.trim();
    }

    private String nullSafe(String value) {
        return nullSafe(value, "");
    }

    private String nullSafe(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private String formatDate(java.util.Date date) {
        if (date == null) {
            return "";
        }
        synchronized (DISPLAY_DATE_FORMAT) {
            return DISPLAY_DATE_FORMAT.format(date);
        }
    }
}
