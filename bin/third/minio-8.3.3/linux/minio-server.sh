echo minio server starting...
# MINIO_ACCESS_KEY=root
# MINIO_SECRET_KEY=ltb12315
chmod a+x ./minio
MINIO_ROOT_USER=root
MINIO_ROOT_PASSWORD=ltb12315
./minio server ./minio-data --console-address ":9001" --address ":9000"