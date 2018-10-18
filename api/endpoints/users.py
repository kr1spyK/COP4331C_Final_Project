from flask import Flask, jsonify, request
from flask import current_app as app
from flask_restful import reqparse, abort, Api, Resource
from database.db import getSession
from database.models import *
from argon2 import PasswordHasher
from argon2 import exceptions


class UserRegisterEndpoint(Resource):
    def post(self):
        required_fields = ["username", "password", "isAdmin"]
        # Get JSON data from request
        json_data = request.get_json(force=True)
        for field in required_fields:
            if field not in json_data.keys():
                return jsonify({"success": -1,
                                "error": "Missing {} field".format(field)})
        # create database session
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])

        # check to see if username is taken
        if session.query(User).filter_by(username=json_data["username"]).first():
            return jsonify({"success": -1,
                            "error": "User already registered"})
        # hash password
        ph = PasswordHasher()
        hash = ph.hash(json_data["password"])

        try:
            new_user = User(username=json_data["username"],
                            password=hash,
                            admin=json_data["isAdmin"])

            session.add(new_user)
            session.commit()
            return jsonify({"success": 1})
        except:
            return jsonify({"success": -1,
                            "error": "Error adding new user to db"})

class LoginEndpoint(Resource):
    def post(self):
        required_fields = ["username", "password"]
        # Get JSON data from request
        json_data = request.get_json(force=True)
        for field in required_fields:
            if field not in json_data.keys():
                return jsonify({"success": -1,
                                "error": "Missing {} field".format(field)})
        # create database session
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])

        # check to see if username is taken
        user = session.query(User).filter_by(username=json_data["username"]).first()
        if not user:
            return jsonify({"success": -1,
                            "error": "User does not exist"})

        try:
            hash = user.password
            ph = PasswordHasher()
            ph.verify(hash, json_data["password"])
            return jsonify({"success": 1})
            # TODO: Create a session in the DB tied to this login
        except exceptions.VerifyMismatchError:
            return jsonify({"success": -1,
                            "error": "Incorrect password provided"})

