from flask import Flask
from flask import current_app as app
from flask_restful import reqparse, abort, Api, Resource
from database.db import getSession
from database.models import *

class TestEndpoint(Resource):
    def get(self):
        return {'hello': 'world', 'Config_Var': app.config['TEST_VAR']}
    def post(self):
        parser = reqparse.RequestParser()
        parser.add_argument('message')
        return parser.parse_args()

class DbTestEndpoint(Resource):
    def get(self):
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])
        testPass = session.query(User).filter_by(username="TestUser").first().password
        return {"Test Users Password": testPass}
