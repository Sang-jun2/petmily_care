import sqlite3

DB_PATH = r"C:\Users\admin\Desktop\최종프로젝트\petmily_care.db"

def get_db_connection():
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row
    return conn