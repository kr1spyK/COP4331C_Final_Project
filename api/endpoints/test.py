from flask import Flask, jsonify, request
from flask import current_app as app
from flask_restful import reqparse, abort, Api, Resource
from database.db import getSession
from database.models import *
from auth import AuthedResource
import json

class TestEndpoint(Resource):
    def get(self):
        return {'hello': 'world', 'Config_Var': app.config['TEST_VAR']}
    def post(self):
        json_data = request.get_json(force=True)
        return jsonify(json_data)

class DbTestEndpoint(Resource):
    def get(self):
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])
        testPass = session.query(User).filter_by(username="TestUser").first().password
        return jsonify({"Test Users Password": testPass})

class TestAuthEndpoint(AuthedResource):
    def get(self):
        return {'hello': 'world', 'Config_Var': app.config['TEST_VAR']}


