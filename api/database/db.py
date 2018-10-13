from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session
from sqlalchemy.orm import sessionmaker
from flask import current_app as app

def getSession( user, passwd):
    Session = sessionmaker(autocommit=False,
                           autoflush=False,
                           bind=create_engine('mysql+pymysql://{}:{}@localhost/app'.format(user, passwd))
                          )
    return scoped_session(Session)
