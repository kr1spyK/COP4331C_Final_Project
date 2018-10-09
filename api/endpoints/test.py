from flask import Flask
from flask import current_app as app
from flask_restful import reqparse, abort, Api, Resource

class TestEndpoint(Resource):
    def get(self):
        return {'hello': 'world', 'Config_Var': app.config['TEST_VAR']}
    def post(self):
        parser = reqparse.RequestParser()
        parser.add_argument('message')
        return parser.parse_args()


