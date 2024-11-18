import io
from PIL import Image
from fastapi import FastAPI, File, UploadFile

from server.app.preprocess import preprocess_image

app = FastAPI()


@app.get("/")
async def root():
    return {"message": "hello"}


@app.post("/chat")
async def chat():
    return "hi"


@app.post("/predict")
async def predict(file: UploadFile = File()):
    # 파일을 바이너리 데이터로 읽기
    file_bytes = await file.read()

    image = io.BytesIO(file_bytes)
    img_array = preprocess_image(image)
    print(img_array)
    return "hi"
