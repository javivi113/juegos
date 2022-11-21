# Funcionalidades
- Filtros.

  - Filtro por defecto, todo(All): Muestra todos los juegos de la API

  Se hara visible el elemento para introducir el filtro, por defecto no estara visible.(SOLO ESTOS SE GUARDAN EN SHAREDPREFENCES)
  - Filtrar por año(Year):  Devolvera todo lo que coincida con el año introducido
  - Filtrar por genero(Genre): Devolvera todo lo que coincida con el genero introducido
  - Filtrar por titulo(Title): Devolvera todo lo que coincida con el titulo introducido
  
- Ultima busqueda realiza.

  En caso de que se haya utilizado un filtro de Year, Title y/o Genre se almacenara mediante sharedpreferences, al darle al boton de Last search cargaran los juegos con el filtro previamente usado
  
- Ver informacion de los juegos.
  
  Al seleccionar el juego, nos mandara a otro layout con toda la informacion del juego, imagenes del juego, requisitos minimos, plataforma, etc...
  Si tocamos el titulo nos redirigirá a la página oficial del juego.
  - Esta página dispone un boton de regresar a la página principal.
