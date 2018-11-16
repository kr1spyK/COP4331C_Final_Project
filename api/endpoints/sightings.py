from flask import Flask, jsonify, request
from flask import current_app as app
from flask_restful import reqparse, abort, Api, Resource
from database.db import getSession
from database.models import *
from auth import AuthedResource
import string
import datetime
import random


class GetSightingsEndpoint(Resource):
    def post(self):
        required_fields = ["id"]
        # Get JSON data from request
        json_data = request.get_json(force=True)
        for field in required_fields:
            if field not in json_data.keys():
                return jsonify({"success": -1,
                                "error": "Missing {} field".format(field)})
        # create database session
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])

        # check to see if username is taken
        try:
            q = session.query(Sighting).filter_by(bugid=json_data["id"]).all()
            sightings = [{"latitude": s.latitude, "longitude": s.longitude} for s in q]
            return jsonify({"success": 1, "sightings": sightings})
        except Exception as e:
            return jsonify({"success": -1,
                            "error": "Error getting sightings: " + str(e)})

class AddSightingsEndpoint(AuthedResource):
    def post(self):
        required_fields = ["id", "latitude", "longitude"]
        # Get JSON data from request
        json_data = request.get_json(force=True)
        for field in required_fields:
            if field not in json_data.keys():
                return jsonify({"success": -1,
                                "error": "Missing {} field".format(field)})
        # create database session
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])

        try:
            new  = Sighting(bugid=json_data["id"],
                            latitude=json_data["latitude"],
                            longitude=json_data["longitude"])

            session.add(new)
            session.commit()
            return jsonify({"success": 1})
        except:
            return jsonify({"success": -1,
                            "error": "Error adding new user to db"})
