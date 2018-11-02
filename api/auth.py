from flask_restful import Resource, abort
from flask import request
from functools import wraps
from database.db import getSession
from database.models import *
from flask import current_app as app


def authenticate(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        if not request.headers.get('X-Auth-Token'):
            abort(401, message="X-Auth-Token header not provided")
        token = request.headers.get('X-Auth-Token')

        # create database session
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])

        # check to see if username is taken
        if not session.query(Session).filter_by(session_id=token).first():
            abort(401, message="Invalid token")
        # TODO: Validate session based on time maybe
        return func(*args, **kwargs)
 
    return wrapper

def authenticateAdmin(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        if not request.headers.get('X-Auth-Token'):
            abort(401, message="X-Auth-Token header not provided")
        token = request.headers.get('X-Auth-Token')

        # create database session
        session = getSession(app.config["DB_USER"], app.config["DB_PASS"])

        # check to see if username is taken
        if not session.query(Session).filter_by(session_id=token).first():
            abort(401, message="Invalid token")

        if not session.query(Session).filter_by(session_id=token).first().user.admin:
            abort(401, message="This resource is for admins only")

        return func(*args, **kwargs)
 
    return wrapper



class AuthedResource(Resource):
    method_decorators = [authenticate]

class AdminAuthedResource(Resource):
    method_decorators = [authenticateAdmin]
