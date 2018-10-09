from flask import Flask
from flask_restful import reqparse, abort, Api, Resource

class TestEndpoint(Resource):
    def get(self):
        return {'hello': 'world'}
    def post(self):
        parser = reqparse.RequestParser()
        parser.add_argument('message')
        return parser.parse_args()


