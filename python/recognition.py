import face_recognition
import json
from flask import Flask, jsonify, request

# You can change this to any folder on your system
ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg', 'gif'}

app = Flask(__name__)


def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


@app.route('/train', methods=['POST'])
def upload_image():
    if 'photo' not in request.files:
        return jsonify({'error': 'bad request'}), 400

    file = request.files['photo']

    if not file or file.filename == '':
        return jsonify({'error': 'bad request'}), 400

    if not allowed_file(file.filename):
        return jsonify({'error': 'bad request'}), 400

    img = face_recognition.load_image_file(file)
    encodings = face_recognition.face_encodings(img)
    if len(encodings) > 0:
        return jsonify({'model': encodings[0].tolist()})

    return jsonify({'error': 'no face detected'}), 400


@app.route('/verify', methods=['POST'])
def detect_faces_in_image():
    if 'photo' not in request.files:
        return jsonify({'error': 'bad request'}), 400
    if 'model' not in request.form:
        return jsonify({'error': 'bad request'}), 400

    face_model = request.form['model']
    face_model = json.loads(face_model)
    file = request.files['photo']

    if not file or file.filename == '':
        return jsonify({'error': 'bad request'}), 400

    if not allowed_file(file.filename):
        return jsonify({'error': 'bad request'}), 400

    img = face_recognition.load_image_file(file)
    unknown_face_encodings = face_recognition.face_encodings(img)

    if len(unknown_face_encodings) > 0:
        match_results = face_recognition.compare_faces([face_model], unknown_face_encodings[0])
        if match_results[0]:
            return jsonify({'verified': True}), 200
    return jsonify({'verified': False}), 200


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5001, debug=True)
