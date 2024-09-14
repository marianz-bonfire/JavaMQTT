/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaMQTT.ui;

import JavaMQTT.utils.MqttUtil;
import javax.swing.*;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author Tarsier
 */
public class MainUI {

    public MqttUtil mqttUtil = new MqttUtil();

    // Declaring UI components as public fields
    public JLabel brokerLabel, clientIdLabel, usernameLabel, passwordLabel, subscribedTopicLabel, logsLabel, topicLabel, messageLabel;
    public JTextField brokerField, clientIdField, usernameField, subscribedTopicField, topicField, messageField;
    public JPasswordField passwordField;
    public JButton subscribeButton, connectButton, disconnectButton, sendButton;
    public JTextArea messageHistory;
    public JScrollPane scrollPane;
    public JList<String> subscribedTopicsList;
    public JScrollPane subscribedTopicsScrollPane;

    // Constructor to initialize the UI components
    public MainUI() {
        // Initialize labels
        brokerLabel = new JLabel("Broker:");
        clientIdLabel = new JLabel("Client ID:");
        usernameLabel = new JLabel("Username (Optional):");
        passwordLabel = new JLabel("Password (Optional):");
        subscribedTopicLabel = new JLabel("Subscribed Topic:");
        topicLabel = new JLabel("Topic:");
        logsLabel = new JLabel("Message Logs:");
        messageLabel = new JLabel("Message:");

        // Initialize text fields
        brokerField = new JTextField("tcp://mqtt.eclipseprojects.io:1883");
        clientIdField = new JTextField("user1");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        subscribedTopicField = new JTextField("Topic 1");
        subscribedTopicField.setEnabled(false); // Disabled as per original image
        topicField = new JTextField("Topic 1");
        messageField = new JTextField();

        // Initialize buttons
        subscribeButton = new JButton("Subscribe");
        connectButton = new JButton("Connect");
        disconnectButton = new JButton("Disconnect");
        disconnectButton.setVisible(false); // Initially invisible
        sendButton = new JButton("Send");

        // Initialize the multiline text area and scroll pane
        messageHistory = new JTextArea(8, 20);
        messageHistory.setEditable(false);
        scrollPane = new JScrollPane(messageHistory);

        // Initialize the subscribed topics list and scroll pane
        subscribedTopicsList = new JList<>(new DefaultListModel<>()); // Use DefaultListModel for easy modification
        subscribedTopicsScrollPane = new JScrollPane(subscribedTopicsList);
    }

    // Method to create and display the UI
    public void show() {
        // Create the frame
        JFrame frame = new JFrame("MQTT Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Increased size to accommodate new listbox

        // Create a panel for arranging components
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Horizontal grouping
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(brokerLabel)
                        .addComponent(clientIdLabel)
                        .addComponent(usernameLabel)
                        .addComponent(passwordLabel)
                        .addComponent(subscribedTopicLabel)
                        .addComponent(topicLabel)
                        .addComponent(logsLabel)
                        .addComponent(messageLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(brokerField)
                        .addComponent(clientIdField)
                        .addComponent(usernameField)
                        .addComponent(passwordField)
                        // Right align Connect and Disconnect buttons
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(connectButton)
                                .addComponent(disconnectButton))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(subscribedTopicField)
                                .addComponent(subscribeButton))
                        .addComponent(subscribedTopicsScrollPane) // Scrollable list of subscribed topics
                        .addComponent(scrollPane)
                        .addComponent(topicField)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(messageField)
                                .addComponent(sendButton)))
        );

        // Vertical grouping
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(brokerLabel)
                        .addComponent(brokerField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(clientIdLabel)
                        .addComponent(clientIdField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(usernameLabel)
                        .addComponent(usernameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passwordLabel)
                        .addComponent(passwordField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(connectButton)
                        .addComponent(disconnectButton)) // Added Disconnect button here
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(subscribedTopicLabel)
                        .addComponent(subscribedTopicField)
                        .addComponent(subscribeButton))
                .addComponent(subscribedTopicsScrollPane) // List of subscribed topics

                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(logsLabel)
                        .addComponent(scrollPane)) // Multiline text area for message history/logs
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(topicLabel)
                        .addComponent(topicField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(messageLabel)
                        .addComponent(messageField)
                        .addComponent(sendButton))
        );

        handleControlEvents();
        // Add the panel to the frame
        frame.add(panel);
        frame.setVisible(true);

    }

    public void handleMessages() {
        mqttUtil.client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                messageHistory.append("Connection lost. \n");
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                messageHistory.append("[" + s + "] : ");
                messageHistory.append(mqttMessage.toString() + "\n");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });
    }

    public void handleControlEvents() {
        subscribedTopicField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                textChanged();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                textChanged();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                textChanged();
            }

            private void textChanged() {
                // This method is called whenever the text changes
                String text = subscribedTopicField.getText();

                if (containsTopic(text)) {
                    subscribeButton.setEnabled(false);
                } else {
                    subscribeButton.setEnabled(true);
                }
            }
        });

        subscribeButton.addActionListener(e -> {
            String topic = subscribedTopicField.getText();
            if (!topic.isEmpty()) {
                if (mqttUtil.subscribeTo(topic)) {
                    addTopic(topic);
                    subscribeButton.setEnabled(false);
                    messageHistory.append("Subscribed to " + topic + "\n");
                } else {
                    subscribeButton.setEnabled(true);
                    messageHistory.append("Failed to subscribe to " + topic + "\n");
                }
            }
        });

        connectButton.addActionListener(e -> {
            connectButton.setEnabled(false);
            String broker = brokerField.getText();
            String clientId = clientIdField.getText();

            if (mqttUtil.connectTo(broker, clientId, usernameField.getText(), new String(passwordField.getPassword()))) {
                messageHistory.append("Successfully connected. \n");
                messageHistory.append("Broker : " + broker + "\n");
                messageHistory.append("Client ID : " + clientId + "\n");
                if (usernameField.getText() != null && !usernameField.getText().isEmpty() && passwordField.getPassword() != null && passwordField.getPassword().length > 0) {
                    messageHistory.append("Username : " + usernameField.getText() + "\n");
                    messageHistory.append("Password : " + new String(passwordField.getPassword()) + "\n");
                }

                DefaultListModel<String> subscribeListModel = (DefaultListModel<String>) subscribedTopicsList.getModel();
                if (!subscribeListModel.isEmpty()) {
                    for (int i = 0; i < subscribeListModel.getSize(); i++) {
                        mqttUtil.subscribeTo(subscribeListModel.get(i));
                    }
                }
                messageHistory.append("Listening for messages...\n");
                brokerField.setEnabled(false);
                clientIdField.setEnabled(false);
                connectButton.setEnabled(true);
                connectButton.setVisible(false);
                disconnectButton.setVisible(true);
                subscribedTopicField.setEnabled(true);
                subscribeButton.setEnabled(true);

                handleMessages();
            } else {
                connectButton.setEnabled(true);
                connectButton.setVisible(true);
                disconnectButton.setVisible(false);
                subscribedTopicField.setEnabled(false);
                subscribeButton.setEnabled(false);
                messageHistory.append("Failed to connect to " + broker + "\n");
            }
        });

        disconnectButton.setVisible(false);
        disconnectButton.addActionListener(e -> {
            if (mqttUtil.disconnectMqttClient()) {
                messageHistory.append("Disconnected from " + brokerField.getText() + "\n");
                brokerField.setEnabled(true);
                clientIdField.setEnabled(true);
                subscribedTopicField.setEnabled(true);
                connectButton.setVisible(true);
                disconnectButton.setVisible(false);
            } else {
                connectButton.setVisible(false);
                disconnectButton.setVisible(true);
                messageHistory.append("Failed to disconnect from " + brokerField.getText() + "\n");
            }
        });

        sendButton.addActionListener(e -> {
            String topic = topicField.getText();
            String message = messageField.getText();

            if (mqttUtil.publishMessage(topic, message)) {
                messageField.setText("");
                messageHistory.append("[" + clientIdField.getText() + "->" + topic + "] : " + message + "\n");
            } else {
                messageHistory.append("Failed to send message. \n");
            }
        });

        subscribedTopicsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String selectedTopic = subscribedTopicsList.getSelectedValue();
                    int index = subscribedTopicsList.getSelectedIndex();
                    System.out.println("Double clicked on Item " + index);
                    int option = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to remove this item?",
                            "Unsubscribe " + selectedTopic,
                            JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        messageHistory.append("Unsubscribed from " + selectedTopic + "\n");
                        subscribedTopicsList.remove(index);
                    }
                }
            }
        });

    }

    public void addTopic(String topic) {
        DefaultListModel<String> model = (DefaultListModel<String>) subscribedTopicsList.getModel();
        if (!model.contains(topic)) {
            model.addElement(topic);
        } else {
            System.out.print("Topic already subscibed: " + topic);
        }
    }

    public Boolean containsTopic(String topic) {
        DefaultListModel<String> model = (DefaultListModel<String>) subscribedTopicsList.getModel();
        return model.contains(topic);
    }
}
