import paho.mqtt.client as mqtt


def on_connect(client, userdata, flags, rc):
    print("MQTT Connected")
    client.subscribe("emergency-topic")


def on_message(client, userdata, msg):
    print(f"Topic: {msg.topic}\nMessage: {msg.payload.decode('utf-8')}")


if __name__ == '__main__':
    client = mqtt.Client()

    client.on_connect = on_connect
    client.on_message = on_message

    client.connect("172.31.253.175", 1883, 60)

    client.loop_forever()