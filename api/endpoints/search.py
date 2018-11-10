from flask import Flask, jsonify, request
from flask import current_app as app
from flask_restful import reqparse, abort, Api, Resource
from database.db import getSession
from database.models import *
from sqlalchemy import or_
import string
import datetime
import random


class SearchEndpoint(Resource):
    def get(self):
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])
        fields = []
        try:
            # Add Common Name
            common_name = {"label": "Common Name",
                           "type": "TEXT"}
            fields.append(common_name)

            # Add Scientific name
            scientific_name = {"label": "Scientific Name",
                               "type": "TEXT"}
            fields.append(scientific_name)

            # Add Class
            c = {"label": "Class",
                 "type": "DROP"}
            c['options'] = [cl.name for cl in session.query(Class).all()]
            fields.append(c)

            # Add Order
            o = {"label": "Order",
                 "type": "DROP"}
            o['options'] = [order.name for order in session.query(Order).all()]
            fields.append(o)

            # Add Familt
            f = {"label": "Family",
                 "type": "DROP"}
            f['options'] = [fa.name for fa in session.query(Family).all()]
            fields.append(f)

            # Add Genus
            g = {"label": "Genus",
                 "type": "DROP"}
            g['options'] = [ge.name for ge in session.query(Genus).all()]
            fields.append(g)

            # Add general type
            gt = {"label": "General Type",
                 "type": "DROP"}
            gt['options'] = [gty.name for gty in session.query(General_Type).all()]
            fields.append(gt)

            # Add mouth parts
            m = {"label": "Mouth Parts",
                 "type": "DROP"}
            m['options'] = [mp.name for mp in session.query(Mouth_Parts).all()]
            fields.append(m)

            # Add color
            colors = {"label": "Colors",
                      "type": "CHECK"}
            colors['options'] = [co.color for co in session.query(Color).all()]
            fields.append(colors)

            # Add wings
            wings = {"label": "Has wings",
                     "type": "DROP"}
            wings['options'] = ["Yes","No"]
            fields.append(wings)

            # Add antenna
            antenna = {"label": "Has antenna",
                       "type": "DROP"}
            antenna['options'] = ["Yes","No"]
            fields.append(antenna)

            # Add hing legs jump
            hl = {"label": "Has hind legs for jumping",
                  "type": "DROP"}
            hl['options'] = ["Yes","No"]
            fields.append(hl)

            # Add hing hair
            hair = {"label": "Has hair",
                    "type": "DROP"}
            hair['options'] = ["Yes","No"]
            fields.append(hair)

            # Add hing thin body
            thin = {"label": "Has thin body",
                    "type": "DROP"}
            thin['options'] = ["Yes","No"]
            fields.append(thin)

            return jsonify({"success": 1,
                            "fields": fields})
        except Exception as e:
            return jsonify({"success": -1,
                            "error": str(e)})

    def post(self):
        # Get JSON data from request
        json_data = request.get_json(force=True)
        # create database session
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])
        try:
            q = session.query(Bug)

            # Filter on common name  
            if "Common Name" in json_data.keys():
                q = q.filter_by(common_name=json_data["Common Name"])
            if "Scientific Name" in json_data.keys():
                q = q.filter_by(scientific_name=json_data["Scientific Name"])
            if "Class" in json_data.keys():
                q = q.filter(Bug._class.has(name=json_data["Class"]))
            if "Order" in json_data.keys():
                q = q.filter(Bug.order.has(name=json_data["Order"]))
            if "Family" in json_data.keys():
                q = q.filter(Bug.family.has(name=json_data["Family"]))
            if "Genus" in json_data.keys():
                q = q.filter(Bug.genus.has(name=json_data["Genus"]))
            if "General Type" in json_data.keys():
                q = q.filter(Bug.general_type.has(name=json_data["General Type"]))
            if "Mouth Parts" in json_data.keys():
                q = q.filter(Bug.mouth_parts.has(name=json_data["Mouth Parts"]))
            if "Colors" in json_data.keys():
                # don't ask
                q = q.filter(or_(x.has(color=c) for c in json_data["Colors"] for x in [Bug.color1, Bug.color2]))
            if "Has wings" in json_data.keys():
                if json_data["Has wings"] == "Yes":
                    q = q.filter_by(wings = True)
                elif json_data["Has wings"] == "No":
                    q = q.filter_by(wings = False)
            if "Has antenna" in json_data.keys():
                if json_data["Has antenna"] == "Yes":
                    q = q.filter_by(antenna= True)
                elif json_data["Has antenna"] == "No":
                    q = q.filter_by(antenna = False)
            if "Has hind legs for jumping" in json_data.keys():
                if json_data["Has hind legs for jumping"] == "Yes":
                    q = q.filter_by(hind_legs_jump= True)
                elif json_data["Has hind legs for jumping"] == "No":
                    q = q.filter_by(hind_legs_jump = False)
            if "Has hair" in json_data.keys():
                if json_data["Has hair"] == "Yes":
                    q = q.filter_by(hairy_furry = True)
                elif json_data["Has hair"] == "No":
                    q = q.filter_by(hairy_furry = False)
            if "Has thin body" in json_data.keys():
                if json_data["Has thin body"] == "Yes":
                    q = q.filter_by(thin_body = True)
                elif json_data["Has thin body"] == "No":
                    q = q.filter_by(thin_body = False)
 


            bugs = [bug.as_dict() for bug in q.all()]

            return jsonify({"success": 1,
                            "results": bugs})

        except Exception as e:
            return jsonify({"success": -1,
                            "error": str(e)})
