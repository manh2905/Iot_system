#include <WiFi.h>
#include <PubSubClient.h>
#include "DHT.h"
#include <ArduinoJson.h>

// ================= WIFI =================
const char* ssid = "M";
const char* password = "88888888";

// ================= MQTT =================
const char* mqtt_server = "172.20.10.4";
const int mqtt_port = 1884;
const char* mqtt_user = "manh";
const char* mqtt_pass = "123456";

// Topic
const char* topic_control = "esp32/devices/control";
const char* topic_status  = "esp32/led/status";
const char* pub_topic     = "esp32/sensor";
const char* pub_reconnect = "esp32/reconnect";

// ================= LED =================
#define LED1 4
#define LED2 5
#define LED3 18
#define LED4 25 // Thêm đèn 4
#define LED5 26 // Thêm đèn 5

// ================= DHT11 =================
#define DHTPIN 14
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

// ================= LDR =================
#define LDR_PIN 34

WiFiClient espClient;
PubSubClient client(espClient);

// ================= WIFI =================
void setupWiFi() {
  Serial.print("Dang ket noi WiFi");
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("\nWiFi da ket noi");
  Serial.println(WiFi.localIP());
}

// ================= MQTT CALLBACK =================
void callback(char* topic, byte* payload, unsigned int length) {

  String message;

  for (int i = 0; i < length; i++) {
    message += (char)payload[i];
  }

  Serial.println("Nhan du lieu:");
  Serial.println(message);

  StaticJsonDocument<512> doc;

  DeserializationError error = deserializeJson(doc, message);

  if (error) {
    Serial.println("JSON parse failed");
    return;
  }

  // Nếu payload là array
  if (doc.is<JsonArray>()) {

    JsonArray arr = doc.as<JsonArray>();

    for (JsonObject obj : arr) {

      String deviceId = obj["deviceId"];
      String command  = obj["command"];

      int ledPin = -1;

      if (deviceId == "1") ledPin = LED1;
      if (deviceId == "2") ledPin = LED2;
      if (deviceId == "3") ledPin = LED3;
      if (deviceId == "4") ledPin = LED4; // Xử lý thiết bị 4
      if (deviceId == "5") ledPin = LED5; // Xử lý thiết bị 5

      if (ledPin == -1) continue;

      if (command == "ON") {
        digitalWrite(ledPin, HIGH);
      }
      else if (command == "OFF") {
        digitalWrite(ledPin, LOW);
      }

      delay(10);

      int state = digitalRead(ledPin);

      String realStatus = (state == HIGH) ? "ON" : "OFF";

      StaticJsonDocument<200> res;

      res["deviceId"] = deviceId;
      res["status"] = realStatus;

      char buffer[200];
      serializeJson(res, buffer);

      client.publish(topic_status, buffer);

      Serial.println("Phan hoi:");
      Serial.println(buffer);
    }
  }

  // Nếu payload chỉ là 1 object
  else if (doc.is<JsonObject>()) {

    JsonObject obj = doc.as<JsonObject>();

    String deviceId = obj["deviceId"];
    String command  = obj["command"];

    int ledPin = -1;

    if (deviceId == "1") ledPin = LED1;
    if (deviceId == "2") ledPin = LED2;
    if (deviceId == "3") ledPin = LED3;
    if (deviceId == "4") ledPin = LED4; // Xử lý thiết bị 4
    if (deviceId == "5") ledPin = LED5; // Xử lý thiết bị 5

    if (ledPin == -1) return;

    if (command == "ON") {
      digitalWrite(ledPin, HIGH);
    }
    else if (command == "OFF") {
      digitalWrite(ledPin, LOW);
    }

    delay(10);

    int state = digitalRead(ledPin);

    String realStatus = (state == HIGH) ? "ON" : "OFF";

    StaticJsonDocument<200> res;

    res["deviceId"] = deviceId;
    res["status"] = realStatus;

    char buffer[200];
    serializeJson(res, buffer);

    client.publish(topic_status, buffer);

    Serial.println("Phan hoi:");
    Serial.println(buffer);
  }
}

// ================= MQTT RECONNECT =================
void reconnectMQTT() {

  while (!client.connected()) {

    Serial.print("Dang ket noi MQTT...");

    if (client.connect("ESP32_CLIENT_01", mqtt_user, mqtt_pass)) {

      Serial.println("Thanh cong");

      client.subscribe(topic_control);

      // ===== PUB reconnect =====
      StaticJsonDocument<100> doc;
      doc["status"] = "reconnect";

      char buffer[100];
      serializeJson(doc, buffer);

      client.publish(pub_reconnect, buffer);

      Serial.println("Da gui reconnect");

    } else {

      Serial.print("That bai, rc=");
      Serial.println(client.state());

      delay(2000);
    }
  }
}

// ================= SETUP =================
void setup() {

  Serial.begin(115200);

  pinMode(LED1, OUTPUT);
  pinMode(LED2, OUTPUT);
  pinMode(LED3, OUTPUT);
  pinMode(LED4, OUTPUT); // Khởi tạo pin D25
  pinMode(LED5, OUTPUT); // Khởi tạo pin D26

  dht.begin();

  setupWiFi();

  client.setServer(mqtt_server, mqtt_port);
  client.setCallback(callback);
}

// ================= LOOP =================
void loop() {

  if (!client.connected()) {
    reconnectMQTT();
  }

  client.loop();

  float h = dht.readHumidity();
  float t = dht.readTemperature();
  int light = 4095 - analogRead(LDR_PIN);

  if (!isnan(h) && !isnan(t)) {

    StaticJsonDocument<200> doc;

    doc["temp"] = t;
    doc["hum"] = h;
    doc["light"] = light;

    char buffer[200];
    serializeJson(doc, buffer);

    client.publish(pub_topic, buffer);
  }

  delay(2000);
}