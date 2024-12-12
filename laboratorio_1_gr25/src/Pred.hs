module Pred (
  Pred,
  cambiar, anyDib, allDib, orP, andP
) where

import Dibujo

type Pred a = a -> Bool

-- Dado un predicado sobre básicas, cambiar todas las que satisfacen
-- el predicado por la figura básica indicada por el segundo argumento.
cambiar :: Pred a -> (a -> Dibujo a) -> Dibujo a -> Dibujo a
cambiar p f d = change (\x -> (if (p x) then (f x) else (figura x))) d

-- Alguna básica satisface el predicado.
anyDib :: Pred a -> Dibujo a -> Bool
anyDib p d = foldr (\x -> \acum -> (p x) || acum) False (figuras d)

-- Todas las básicas satisfacen el predicado.
allDib :: Pred a -> Dibujo a -> Bool
allDib p d = foldr (\x -> \acum -> (p x) && acum) True (figuras d)

-- Los dos predicados se cumplen para el elemento recibido.
andP :: Pred a -> Pred a -> Pred a
andP p0 p1 = \x -> (p0 x) && (p1 x) 

-- Algún predicado se cumple para el elemento recibido.
orP :: Pred a -> Pred a -> Pred a
orP p0 p1 = \x -> (p0 x) || (p1 x) 