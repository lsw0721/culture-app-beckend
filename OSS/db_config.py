# db_config.py

import mysql.connector

def get_connection():
    return mysql.connector.connect(
        host='localhost',
        user='root',
        password='kwon0822@',
        database='culture_db',
        charset='utf8mb4'
    )
