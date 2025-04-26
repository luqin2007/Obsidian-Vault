from manim import *


class CreateCircle(Scene):
    def construct(self):
        circle = Circle()
        circle.set_fill(PINK, opacity=0.5) # 填充色
        self.play(Create(circle))          # 画圆
