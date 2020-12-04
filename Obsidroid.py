import socket  
import serial
ser = serial.Serial(port='COM8',baudrate=9600)             

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)        
host = socket.gethostbyname(socket.gethostname()) 
print('host:',host)
port = 12345            # Reserve a port for your service.
s.bind((host, port))        # Bind to the port

while True:
    print("Waiting for connection")
    s.listen(1)                 # Now wait for client connection.
    c, addr = s.accept()        # Establish connection with client.
    print('Got connection from ', addr)
    while True:
        a=c.recv(1024).decode('utf-8')
        if "close" in a:
            c.close()
            print("Connection closed")
            break
        elif len(a)==0:
            print("No message received")
            c.close()
            break
        a=a.split("\n")[0]
        if a.isnumeric():
            a=int(a).to_bytes(3,'big')
            ser.write(a)
        print(a)

