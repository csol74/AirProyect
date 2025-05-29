![image](https://github.com/user-attachments/assets/dd34eee2-f39d-469a-a7c7-18bfb878fb6a)

Creamos el repositorio donde tendremos almacenado en python la logica necesaria para guardar el modelo y llamarlo en la app de Android Studio

![image](https://github.com/user-attachments/assets/bb8a601a-0d92-4702-9e82-012a94978682)

Se suben los archivo necesarios como los modelos, el escaler y el requirements que permitira instalar todas las librerias y dependencias necesarias para el buen funcionamiento en Railway, donde estara almacenado el modelo que llamaremos en la app

![image](https://github.com/user-attachments/assets/90b108a3-9659-4e8f-81f0-caea07d4d2a8)

Ya en Railway le daremos implementar nuevo proyecto desde github

![image](https://github.com/user-attachments/assets/a609fdb5-e6de-4045-b75d-9461a177f7e3)

Se elige el repositorio creado donde esta almacenado el archivo de python con los modelos cargados y la plataforma automaticamente empiza la conexión 

![image](https://github.com/user-attachments/assets/6d858a67-9402-4864-b267-be7f5c73ec1c)

Para realizar la conexión en Android studio primero vamos build.gradle.kts (module app), se implementan las dependencias necesarias para la app 

![image](https://github.com/user-attachments/assets/3c9f403c-02ab-451c-b6f7-b651e97ed4cf)

En el archivo manifest se agrega un permiso necesario para conectar a internet

![image](https://github.com/user-attachments/assets/47d044e3-f565-40a0-ae61-b56ae8019cf1)

Finalmente AirScreen.kt define la interfaz de usuario en Jetpack Compose donde se ingresan datos de calidad del aire y se muestra la predicción; PredictionData.kt y PredictionResponse.kt son data classes que modelan, respectivamente, los datos enviados a la API y la respuesta recibida; ApiService.kt declara la interfaz de Retrofit que describe las peticiones HTTP; y RetrofitClient.kt configura el cliente Retrofit para conectarse con la API.

![image](https://github.com/user-attachments/assets/aee43074-c221-479e-a4ff-cfe7046f317b)


App:

![image](https://github.com/user-attachments/assets/af33c0c9-73ef-4b8a-842b-3ad840b2b6dc)

















