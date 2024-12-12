module Main (main) where

import Test.HUnit
import Dibujo
import qualified System.Exit as Exit
-- import GHC.Num (integerAnd, integerOr)

--Como todas las funciones son polimórficas las pobé con números/strings 

--Recordar: 
--assertEqual es una función que se utiliza para verificar si dos valores son iguales. 
--Toma tres argumentos: un mensaje de error, el valor actual y el valor esperado.
--Tambien recordar: When an assertion is evaluated, it will output a message if and only if the assertion fails.

testComp :: Test
testComp = TestList[ 
      --Caso base
      TestCase (assertEqual "Error en testComp con n=0" (figura 8) (comp 0 (rot45) (figura 8))),
      TestCase (assertEqual "Error en testComp con n>0" (rotar (figura 8)) (comp 1 (rotar) (figura 8))),
      TestCase (assertEqual "Error en testComp con n>0" (r90 (figura 8)) (comp 1 (rotar) (figura 8))),
      TestCase (assertEqual "Error en testComp con n>0" (r180 (figura 8)) (comp 2 (rotar) (figura 8))),
      TestCase (assertEqual "Error en testComp con n>0" (r270 (figura 8)) (comp 3 (rotar) (figura 8)))
    ]

testFigura :: Test
testFigura = TestCase (assertEqual "Error en testFigura" (figura 3) (Figura 3))

testEncimar :: Test
testEncimar = TestCase (assertEqual "Error en testEncimar" (encimar (Figura 1) (Figura 2)) (Encimar (Figura 1) (Figura 2)))

testApilar :: Test
testApilar = TestCase (assertEqual "Error en testApilar" (apilar 1.0 1.0 (Figura 1) (Figura 2)) (Apilar 1.0 1.0 (Figura 1) (Figura 2)))

testJuntar :: Test
testJuntar = TestCase (assertEqual "Error en testJuntar" (juntar 1.0 1.0 (Figura 1) (Figura 2)) (Juntar 1.0 1.0 (Figura 1) (Figura 2)))

testRot45 :: Test
testRot45 = TestCase (assertEqual "Error en testRot45" (rot45 (Figura 1)) (Rot45 (Figura 1)))

testRotar :: Test
testRotar = TestCase (assertEqual "Error en testRotar" (rotar (Figura 1)) (Rotar (Figura 1)))

testEspejar :: Test 
testEspejar = TestCase (assertEqual "Error en testEspejar" (espejar (Figura 1)) (Espejar (Figura 1)))

testR90 :: Test
testR90 = TestCase (assertEqual "Error en r90" (r90 (figura 4)) (Rotar (figura 4)) )

testR180 :: Test
testR180 = TestCase (assertEqual "Error en r180" (r180 (figura 4)) (Rotar (Rotar (figura 4)) ))

testR270 :: Test
testR270 = TestCase (assertEqual "Error en r270" (r270 (figura 4)) (Rotar (Rotar (Rotar (figura 4) ) ) ) )

testrEncimar4 :: Test
testrEncimar4 = TestCase (assertEqual "Error en testEncimar4 " (encimar (encimar (encimar (figura 4) (rotar (figura 4))) (rotar (rotar (figura 4)))) (rotar (rotar (rotar (figura 4))))) (encimar4 (figura 4)) )

testCuarteto :: Test
testCuarteto = TestCase (assertEqual "Error en testCuarteto" ( apilar 1.0 1.0 (juntar 1.0 1.0 (figura 1) (figura 2)) (juntar 1.0 1.0 (figura 3) (figura 4)) ) (cuarteto (figura 1) (figura 2) (figura 3) (figura 4) ) )

testCiclar :: Test
testCiclar = TestCase (assertEqual "Error en testCiclar" (Apilar 1.0 1.0 (Juntar 1.0 1.0 (Figura 8) (Rotar (Figura 8))) (Juntar 1.0 1.0 (Rotar (Rotar (Figura 8))) (Rotar (Rotar (Rotar (Figura 8)))))) (ciclar (figura 8)) )

testMap :: Test 
testMap = TestList[
    TestCase (assertEqual "Error en testMap caso base con id" (Figura 2) (mapDib (id) (Figura 2)) ),
    TestCase (assertEqual "Error en testMap caso base" (Figura 4) (mapDib (+2) (Figura 2)) ),
    TestCase (assertEqual "Error en testMap en Rot45" (Rot45 (Figura 6)) (mapDib (*2) (Rot45 (Figura 3))) ),
    TestCase (assertEqual "Error en testMap en Rotar" (Rotar (Figura 8)) (mapDib (+4) (Rotar (Figura 4))) ),
    TestCase (assertEqual "Error en testMap en Espejar" (Espejar (Figura 11)) (mapDib (\x -> x-10) (Espejar (Figura 21))) ),
    TestCase (assertEqual "Error en testMap en Apilar" (Apilar 1.0 1.0 (Figura 23) (Figura 33)) (mapDib (+20) (Apilar 1.0 1.0 (Figura 3) (Figura 13))) ),
    TestCase (assertEqual "Error en testMap en Juntar" (Juntar 1.0 1.0 (Figura 20) (Figura 30)) (mapDib (*10) (Juntar 1.0 1.0 (Figura 2) (Figura 3))) ),
    TestCase (assertEqual "Error en testMap en Encimar" (Encimar (Figura 20) (Figura 30)) (mapDib (\x -> x-10) (Encimar (Figura 30) (Figura 40))) )
    ] 

testChange :: Test
testChange = TestList [
    TestCase (assertEqual "Error en change caso base" (Figura 6) (change (\n -> Figura (2*n)) (Figura 3))),
    TestCase (assertEqual "Error en change caso Rot45" (Rot45 (Figura 6)) (change (\n -> Figura (2*n)) (Rot45 (Figura 3)))),
    TestCase (assertEqual "Error en change caso Rotar" (Rotar (Figura 0)) (change (\n -> Figura (n-1)) (Rotar (Figura 1)))),
    TestCase (assertEqual "Error en change caso Espejar" (Espejar (Figura 16)) (change (\n -> Figura (n*8)) (Espejar (Figura 2)))),
    TestCase (assertEqual "Error en change caso Apilar" (Apilar 1.0 1.0 (Figura 16) (Figura 12)) (change (\n -> Figura (n+6)) (Apilar 1.0 1.0 (Figura 10) (Figura 6))) ),
    TestCase (assertEqual "Error en change caso Juntar" (Juntar 1.0 1.0 (Figura 20) (Figura 25)) (change (\n -> Figura (n*5)) (Juntar 1.0 1.0 (Figura 4) (Figura 5))) ),
    TestCase (assertEqual "Error en change caso Encimar" (Encimar (Figura 10) (Figura 20)) (change (\n -> Figura (n-4)) (Encimar (Figura 14) (Figura 24))))
    ]

testFoldDib :: Test
testFoldDib = TestList[
    TestCase (assertEqual "Error en testFoldDib con Figura" 5 (foldDib id id id id (\x y z w -> x) (\x y z w -> x) (\x y -> x) (Figura 5))), 
    TestCase (assertEqual "Error en testFoldDib con Rotar" 5 (foldDib id id (\x -> x) id (\x y z w -> x) (\x y z w -> x) (\x y -> x) (Rotar (Figura 5)))), 
    TestCase (assertEqual "Error en testFoldDib con Apilar" 1 (foldDib id id id id (\x y z w -> x) (\x y z w -> x) (\x y -> x) (Apilar 1.0 1.0 (Figura 5) (Figura 6))))
  ]

tests :: Test
tests = TestList [
        TestLabel "testComp" testComp,
        TestLabel "testFigura" testFigura, 
        TestLabel "testEncimar" testEncimar,
        TestLabel "testApilar" testApilar,
        TestLabel "testJuntar" testJuntar,
        TestLabel "testRot45" testRot45,
        TestLabel "testRotar" testRotar,
        TestLabel "testEspejar" testEspejar,
        TestLabel "testR90" testR90,
        TestLabel "testR180" testR180,
        TestLabel "testR270" testR270,
        TestLabel "testEncimar4" testEncimar,
        TestLabel "testCuarteto" testCuarteto,
        TestLabel "testCiclar" testCiclar,
        TestLabel "testMap" testMap,
        TestLabel "testChange" testChange,
        TestLabel "testFoldDib" testFoldDib
        ]

-- Usar cabal test dibujo --test-show-details=always
main :: IO ()
main = do
    result <- runTestTT tests
    if failures result > 0 then Exit.exitFailure else Exit.exitSuccess