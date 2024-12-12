# Experiencia
Al estar resolviendo el laboratorio nos dimos cuenta de que habia dos definiciones de column y row para grilla, una se encontraba en `Dibujos/Grilla.hs` y otra en `Dibujos/Feo.hs`, por lo que decidimos cambiar alguna para unificarla pues por alguna razón ambas daban distintos resultados de interpretaciones. En ambos archivos dejamos la de `Dibujos/Feo.hs` ya que nos pareció más entendible y que daba la interpretación que buscabamos.

# Preguntas

### *¿Por qué están separadas las funcionalidades en los módulos indicados? Explicar detalladamente la responsabilidad de cada módulo.*

La separación de las funcionalidades en los distintos módulos nos permite tener una mejor comprensión del código pues se vuelve más legible y nos permite dividir las acciones que cada uno hace.De esta forma nos podemos centar primeramente en el planteamiento de las figuras en cuanto a su sintaxis y luego preocuparnos de su implementación e interpretación gráfica.
Por otro lado al ser módulos independientes entre sí podemos testear sus funcionalidades por separado.

**==== MODULOS ====**
- Dibujo.hs: Se define la sintaxis del lenguaje a usar, proporcionando un conjunto de funciones constructoras y auxiliares además del tipo de dato Dibujo.
- Pred.hs: Define el tipo de dato Pred el cual termina funcionando como una herramienta para trabajar combinando predicados con los Dibujos antes definidos, esto da lugar a que se puedan modificar los dibujos solo bajo ciertas condiciones y también que se puedan verificar ciertas propiedades de ellos.
- Interp.hs: Se encarga de ejecutar la semántica de nuestro idioma, dando una interpretación concreta y visual de los dibujos hechos mediante el uso de vectores geométricos.
- FlotingPic.hs: Brinda las herramientas para crear y manipular las imágenes flotantes en la biblioteca Graphics.Gloss.
- Main.hs: Este módulo nos deja llamar a todos los otros módulos descriptos ante y también configurarlos para posteriormente realizar representaciones gráficas por medio de Gloss.

### *¿Por qué las figuras básicas no están incluidas en la definición del lenguaje, y en vez de eso, es un parámetro del tipo?*

Las figuras básicas no están incluídas allí porque esto brinda más flexibilidad a los dibujos que queramos hacer ya que podemos difinir variados tipos de figuras con las que luego podemos hacer el dibujo. Esto es el por qué básicamente definimos el tipo de dato Dibujo como algo polimórfico, para que las figuras básicas se puedan ir redefiniendo según el dibujo que se esté creando.

### *¿Qué ventaja tiene utilizar una función de `fold` sobre hacer pattern-matching directo?*

Fold permite expresar la lógica de forma más concisa (debido a que genera una abstracción) y es mucho más fácil de entender, por otra parte brinda una mayor flexibilidad al poder trabajar con los diferentes elementos del dibujo realizando operaciones genéricas.

### *¿Cuál es la diferencia entre los predicados definidos en Pred.hs y los tests*
La diferencia es que los predicados permitidos en `Pred.hs` son en cierto punto libres, pues mientras generen una condición booleana son correctosy pueden ser evaluados. Mientras que en los **tests** ocurre que estos son forzados para que los tests no fallen y puedan corroborar la funcionalidad de los programas realizados.