# cinema-api

Para acessar a API apenas baixe este código e importe ele como um projeto Maven na sua IDE (Recomendo o Eclipse)

1 Vá no arquivo application.properties e lá você vai encontrar o acesso ao banco de dados. Substitua o que está escrito pela informação na tela.

2 A aplicação utiliza o serviço da AmazonS3 para salvar a imagem, poranto é necessário ter uma access key e o segredo da mesma.
2.1 Em application.properties você verá duas linhas de texto comentadas, no lugar delas digite:  

"cinema.s3.access-key-id:(aqui você digita a sua chave)" 
"cinema.s3.secret-access-key:(aqui o segredo da chave)".
  *sem as áspas.
  Após isso apenas inicie a aplicação e lembre-se de verificar se a mesma está na porta 8080. Caso contrário terá que mudar no front-end a porta a chamar a API.
