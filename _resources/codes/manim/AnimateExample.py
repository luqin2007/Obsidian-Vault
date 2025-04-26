from manim import *


class AnimateExample(Scene):
    def construct(self):
        square = Square().set_fill(RED, opacity=1)
        self.add(square)

        self.play(square.animate.set_fill(WHITE))
        self.wait(1)

        self.play(square.animate.shift(UP).rotate(PI / 3))
        self.wait(1)