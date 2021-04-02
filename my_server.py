#!/usr/bin/env python3
"""
Very simple HTTP server in python for logging requests
Usage::
    ./server.py [<port>]
"""
from http.server import BaseHTTPRequestHandler, HTTPServer
import logging

import base64

def find_between( s, first, last ):
		try:
			start = s.index( first ) + len( first )
			end = s.index( last, start )
			return s[start:end]
		except ValueError:
			return ""
			
class S(BaseHTTPRequestHandler):

			
	def _set_response(self):
		self.send_response(200)
		self.send_header('Content-type', 'text/html')
		self.end_headers()

	def do_GET(self):
		logging.info("GET request,\nPath: %s\nHeaders:\n%s\n", str(self.path), str(self.headers))
		self._set_response()
		self.wfile.write("GET request for {}".format(self.path).encode('utf-8'))

	def do_POST(self):
		content_length = int(self.headers['Content-Length']) # <--- Gets the size of data
		post_data = self.rfile.read(content_length) # <--- Gets the data itself
        # parse the Body for photo base64 data
		base64Photo=find_between( post_data.decode('utf-8'), "form-data; name=\"photo\"", "------WebKitFormBoundary" ).strip()
		# decode base64 
		imgdata = base64.b64decode(base64Photo) 
		filename = 'To-Process.jpg' # <--- we could use DOC Number as filename
		with open(filename, 'wb') as f: # write binary
			f.write(imgdata) # <--- we can go and run face_recognition on received Image
			
		print("===========================\n")
		#print(base64Photo)
		print("===========================\n")
		# RAW output of whats received from the Client:
		logging.info("POST request,\nPath: %s\nHeaders:\n%s\n\nBody:\n%s\n",
                str(self.path), str(self.headers), post_data.decode('utf-8'))

		self._set_response()
		self.wfile.write("POST request for {}".format(self.path).encode('utf-8'))




			
def run(server_class=HTTPServer, handler_class=S, port=8080):
    logging.basicConfig(level=logging.INFO)
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    logging.info('Starting httpd...\n')
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()
    logging.info('Stopping httpd...\n')

if __name__ == '__main__':
    from sys import argv

    if len(argv) == 2:
        run(port=int(argv[1]))
    else:
        run()
