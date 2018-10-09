from flask import Flask
from flask_restful import reqparse, abort, Api, Resource
from endpoints.test import *

app = Flask(__name__)
app.config.from_pyfile('api.cfg')
api = Api(app)

api.add_resource(TestEndpoint, '/test')

if __name__ == '__main__':
    app.run(debug=True, port=8080)
