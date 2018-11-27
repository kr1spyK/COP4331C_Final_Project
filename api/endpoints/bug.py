from auth import *
from flask import Flask, jsonify, request
from flask import current_app as app
from flask_restful import reqparse, abort, Api, Resource
from database.db import getSession
from database.models import *
import json

class BugRegisterEndpoint(AdminAuthedResource):
    def post(self):
        
        # Define required fields
        required_fields = ["common_name", "scientific_name", "class",
        "order", "family", "genus", "color_1", "color_2", "general_type", 
        "mouth_parts", "wings", "antenna", "hind_legs_jump", "hairy_furry",
        "thin_body", "description", "additional_advice"]
        
        # Get JSON data from request
        # If any required field is missing, the missing field will be sent back in a JSON response
        json_data = request.get_json(force=True)
        for field in required_fields:
            if field not in json_data.keys():
                return jsonify({"success": -1,
                                "error": "Missing {} field".format(field)})

        # Create database session
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])

        # Check to see if bug entry exists
        if session.query(Bug).filter_by(common_name=json_data["common_name"]).first():
            return jsonify({"success": -1,
                            "error": "Bug already registered"})

        # If there isn't a bug entry, make a new bug from the JSON data
        # and attempt to make a request to the database to add it
        try:
            _class = session.query(Class).filter_by(name=json_data["class"]).first()
            if not _class:
                new_class = Class(name=json_data["class"])
                session.add(new_class)
                session.commit()
                _class = new_class

            order = session.query(Order).filter_by(name=json_data["order"]).first()
            if not order:
                new_order = Order(name=json_data["order"])
                session.add(new_order)
                session.commit()
                order = new_order

            family = session.query(Family).filter_by(name=json_data["family"]).first()
            if not family:
                new_family = Family(name=json_data["family"])
                session.add(new_family)
                session.commit()
                family = new_family

            genus = session.query(Genus).filter_by(name=json_data["genus"]).first()
            if not genus:
                new_genus = Genus(name=json_data["genus"])
                session.add(new_genus)
                session.commit()
                genus = new_genus

            color1 = session.query(Color).filter_by(color=json_data["color_1"]).first()
            if not color1:
                new_color1 = Color(color=json_data["color_1"])
                session.add(new_color1)
                session.commit()
                color1 = new_color1

            color2 = session.query(Color).filter_by(color=json_data["color_2"]).first()
            if not color2:
                new_color2 = Color(color=json_data["color_2"])
                session.add(new_color2)
                session.commit()
                color2 = new_color2

            general_type = session.query(General_Type).filter_by(name=json_data["general_type"]).first()
            if not general_type:
                new_general_type = General_Type(name=json_data["general_type"])
                session.add(new_general_type)
                session.commit()
                general_type = new_general_type

            mouth_parts = session.query(Mouth_Parts).filter_by(name=json_data["mouth_parts"]).first()
            if not mouth_parts:
                new_mouth_parts = Mouth_Parts(name=json_data["mouth_parts"])
                session.add(new_mouth_parts)
                session.commit()
                mouth_parts = new_mouth_parts



            new_bug = Bug(common_name=json_data["common_name"],
                          scientific_name=json_data["scientific_name"],
                          class_id=_class.id,
                          order_id=order.id,
                          family_id=family.id,
                          genus_id=genus.id,
                          color_id_1=color1.id,
                          color_id_2=color2.id,
                          general_type_id=general_type.id,
                          mouth_parts_id=mouth_parts.id,
                          wings=json_data["wings"],
                          antenna=json_data["antenna"],
                          hind_legs_jump=json_data["hind_legs_jump"],
                          hairy_furry=json_data["hairy_furry"],
                          thin_body=json_data["thin_body"],
                          description=json_data["description"],
                          additional_advice=json_data["additional_advice"],
                          approved=False)
            session.add(new_bug)
            session.commit()

            # If successful, return 1, if not return -1 and error status
            return jsonify({"success": 1})
        except Exception as e:
            return jsonify({"success": -1, 
                            "error": str(e)})
