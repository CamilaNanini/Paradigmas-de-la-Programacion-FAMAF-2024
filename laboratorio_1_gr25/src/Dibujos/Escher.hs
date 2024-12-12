module Dibujos.Escher where

import Dibujo
import Interp
import Graphics.Gloss
import FloatingPic(Conf(..), Output, half, zero)
import qualified Graphics.Gloss.Data.Point.Arithmetic as V

-- Supongamos que eligen.
data Basica = Triangulo | Blanco deriving (Show, Eq)
type Escher = Basica

interpEscher :: Output Escher
interpEscher Triangulo d w h = line $ map (d V.+) [(0,0), h, w, (0,0)]
interpEscher Blanco d w h = blank

escherConf :: Conf
escherConf = Conf {
    name = "Escher"
    , pic = escher 2 Triangulo
    , bas = interpEscher
}

-- El dibujo u.
dibujoU :: Dibujo Escher -> Dibujo Escher
dibujoU p = encimar (encimar p2 (rotar p2)) (encimar (r180 p2) (r270 p2))
        where 
            p2 = espejar (rot45 p)

-- El dibujo t.
dibujoT :: Dibujo Escher -> Dibujo Escher
dibujoT p = encimar p (encimar p2 p3)
    where
        p2 = espejar(rot45 p)
        p3 = r270 p2

-- Esquina con nivel de detalle en base a la figura p.
esquina :: Int -> Dibujo Escher -> Dibujo Escher
esquina 1 p = cuarteto (figura Blanco) (figura Blanco) (figura Blanco) (dibujoU p)
esquina n p = cuarteto (esquina (n-1) p) (lado (n-1) p) (rotar(lado (n-1) p)) (dibujoU p)

-- Lado con nivel de detalle.
lado :: Int -> Dibujo Escher -> Dibujo Escher
lado 1 p = cuarteto (figura Blanco) (figura Blanco) (rotar (dibujoT p)) (dibujoT p)
lado n p = cuarteto (lado (n-1) p) (lado (n-1) p) (rotar (dibujoT p)) (dibujoT p)

-- Por suerte no tenemos que poner el tipo!
noneto p q r s t u v w x = apilar 1.0 2.0 (juntar 1.0 2.0 p (juntar 1.0 1.0 q r)) 
                            (apilar 1.0 1.0 (juntar 1.0 2.0 s (juntar 1.0 1.0 t u)) (juntar 1.0 2.0 v (juntar 1.0 1.0 w x)))

-- El dibujo de Escher:
escher :: Int -> Escher -> Dibujo Escher
escher n tile = noneto p q r s t u v w x
    where
        fig = figura tile
        p = esquina n fig
        q = lado n fig
        r = r270 (esquina n fig)
        s = rotar (lado n fig)
        t = dibujoU fig
        u = r270 (lado n fig)
        v = rotar (esquina n fig)
        w = r180 (lado n fig)
        x = r180 (esquina n fig)
