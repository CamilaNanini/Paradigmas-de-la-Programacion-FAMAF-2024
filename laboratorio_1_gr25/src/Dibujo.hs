module Dibujo (encimar,
    figura,
    apilar,
    juntar,
    rot45,
    rotar,
    r90,
    r180,
    r270,
    espejar,
    change,
    encimar4,
    cuarteto,
    comp,
    ciclar,
    figuras,
    foldDib,
    mapDib,
    
    Dibujo(..)
    ) where


-- nuestro lenguaje 
data Dibujo a = Figura a
                | Encimar (Dibujo a) (Dibujo a)
                | Apilar Float Float (Dibujo a) (Dibujo a)
                | Juntar Float Float (Dibujo a) (Dibujo a)
                | Rot45 (Dibujo a)
                | Rotar (Dibujo a)
                | Espejar (Dibujo a)
                deriving (Eq, Show)


-- combinadores
infixr 6 ^^^

infixr 7 .-.

infixr 8 ///

comp :: Int -> (a -> a) -> a -> a
comp 0 f = id
comp n f = f . (comp (n-1) f)

-- Funciones constructoras
figura :: a -> Dibujo a
figura = Figura

encimar :: Dibujo a -> Dibujo a -> Dibujo a
encimar = Encimar

apilar :: Float -> Float -> Dibujo a -> Dibujo a -> Dibujo a
apilar = Apilar

juntar  :: Float -> Float -> Dibujo a -> Dibujo a -> Dibujo a
juntar = Juntar -- venia dada

rot45 :: Dibujo a -> Dibujo a
rot45 = Rot45

rotar :: Dibujo a -> Dibujo a
rotar = Rotar

espejar :: Dibujo a -> Dibujo a
espejar = Espejar

-- Superpone un dibujo con otro.
(^^^) :: Dibujo a -> Dibujo a -> Dibujo a
(^^^) a b = Encimar a b

-- Pone el primer dibujo arriba del segundo, ambos ocupan el mismo espacio.
(.-.) :: Dibujo a -> Dibujo a -> Dibujo a
(.-.) a b = Apilar 1.0 1.0 a b

-- Pone un dibujo al lado del otro, ambos ocupan el mismo espacio.
(///) :: Dibujo a -> Dibujo a -> Dibujo a
(///) a b = Juntar 1.0 1.0 a b

-- rotaciones
r90 :: Dibujo a -> Dibujo a
r90 = rotar

r180 :: Dibujo a -> Dibujo a
r180 = comp 2 rotar

r270 :: Dibujo a -> Dibujo a
r270 = comp 3 rotar 

-- una figura repetida con las cuatro rotaciones, superimpuestas.
encimar4 :: Dibujo a -> Dibujo a
encimar4 d = (comp 3 (\x -> encimar d (rotar x))) d

-- cuatro figuras en un cuadrante.
cuarteto :: Dibujo a -> Dibujo a -> Dibujo a -> Dibujo a -> Dibujo a
cuarteto a b c d = (.-.) ((///) a b) ((///) c d)

-- un cuarteto donde se repite la imagen, rotada (¡No confundir con encimar4!)
ciclar :: Dibujo a -> Dibujo a
ciclar a = (.-.) ((///) a (r90 a)) ((///) (r180 a) (r270 a))

-- map para nuestro lenguaje
mapDib :: (a -> b) -> Dibujo a -> Dibujo b
mapDib f (Figura a) = figura (f a)
mapDib f (Rot45 d) = rot45 (mapDib f d)
mapDib f (Rotar d) = rotar (mapDib f d)
mapDib f (Espejar d) = espejar (mapDib f d)
mapDib f (Apilar x y d0 d1) = apilar x y (mapDib f d0) (mapDib f d1)
mapDib f (Juntar x y d0 d1) = juntar x y (mapDib f d0) (mapDib f d1)
mapDib f (Encimar d0 d1) = encimar (mapDib f d0) (mapDib f d1)

-- verificar que las operaciones satisfagan
-- 1. map figura = id
-- 2. map (g . f) = mapDib g . mapDib f
--Aplicar una función a cada elemento de una lista

-- Cambiar todas las básicas de acuerdo a la función.
change :: (a -> Dibujo b) -> Dibujo a -> Dibujo b
change f (Figura a) = f a
change f (Rot45 d) = rot45 (change f d)
change f (Rotar d) = rotar (change f d)
change f (Espejar d) = espejar (change f d)
change f (Apilar x y d0 d1) = apilar x y (change f d0) (change f d1)
change f (Juntar x y d0 d1) = juntar x y (change f d0) (change f d1)
change f (Encimar d0 d1) = encimar (change f d0) (change f d1)

-- Principio de recursión para Dibujos.
foldDib ::
  (a -> b) ->
  (b -> b) ->
  (b -> b) ->
  (b -> b) ->
  (Float -> Float -> b -> b -> b) ->
  (Float -> Float -> b -> b -> b) ->
  (b -> b -> b) ->
  Dibujo a ->
  b
foldDib figuraD rot45D rotarD espejarD apilarD juntarD encimarD dib =
  case dib of
    Figura x -> figuraD x
    Rot45 d -> rot45D (fDib d)
    Rotar d -> rotarD (fDib d)
    Espejar d -> espejarD (fDib d)
    Apilar x0 x1 d0 d1 -> apilarD x0 x1 (fDib d0) (fDib d1)
    Juntar x0 x1 d0 d1 -> juntarD x0 x1 (fDib d0) (fDib d1)
    Encimar d0 d1 -> encimarD (fDib d0) (fDib d1)
  where
    fDib = foldDib figuraD rot45D rotarD espejarD apilarD juntarD encimarD

-- Extrae todas las figuras básicas de un dibujo.
figuras :: Dibujo a -> [a]
figuras d = foldDib (:[]) ([]++) ([]++) ([]++) (concat) (concat) (\b0 b1 -> b0++b1) d
  where
    concat = \ x y b0 b1 -> b0++b1