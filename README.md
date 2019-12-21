This project is a simulator to design and fly space missions. The final goal is to be able to give the simulator a set of files containing information about the system (planets, stars, asteroids) and the spacecraft(s) (including the program of the on board computer).  
Then the user can launch the mission that will run automatically following the program he put in the board computer. During the mission he can only move the camera (everything is rendered in 3D with OpenGL) or accelerate/decelerate the time.  

During development, every possible upgrade must be taken into account to ease future improvements. For example the function giving the time between 2 physics updates takes into account the space time dilatation even tho it is always 1 for the moment.

The objects in the simulation world are called mobiles. A mobile alone is just a physics object: forces and collisions can act on it etc. Any action done by a mobile is done by its modules. A module has no hitbox and remains at a fixed local position relative to its parent mobile. But it can do things like emitting light, containing fuel, producing thrust, changing values on other modules (e.g turn on an engine).

### Development progress
- Maths library (Vectors, Quaternions/EulerAngles and Matrices arithmetics)  
- Base of the graphics part (3D model rendering, simple lighting)  
- Mobiles and modules development is started but there are a lot of things to do.  

### Major things to do
- Planets rendering  
- Graphics: 3D render improvements (shadows etc) + UI  
- Physics: Modules, Aerodynamics etc  
- Collisions  

### Image
This image shows a cube orbiting a very heavy Suzanne.
The cube contains a light source module that is turn on and off every 2 seconds by the board computer module.
![CubeAndSuzanne](https://i.imgur.com/bUvoy3c.png)
