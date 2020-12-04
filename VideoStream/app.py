#!/usr/bin/env python
import socket
from flask import Flask, render_template, Response

# import camera driver
# if os.environ.get('CAMERA'):
#    Camera = import_module('camera_' + os.environ['CAMERA']).Camera
# else:
#    from camera import Camera

# Raspberry Pi camera module (requires picamera package)
from camera import Camera

app = Flask(__name__)


@app.route('/')
def index():
    """Live streaming"""
    return render_template('index.html')


def gen(camera):
    """Video streaming generator function."""
    while True:
        frame = camera.get_frame()
        yield (b'--frame\r\n'
               b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n')


@app.route('/video_feed')
def video_feed():
    """Video streaming route. Put this in the src attribute of an img tag."""
    return Response(gen(Camera()),
                    mimetype='multipart/x-mixed-replace; boundary=frame')


if __name__ == '__main__':
    host = socket.gethostbyname(socket.gethostname())
    app.run(host=host, threaded=True)
