# MyAguilaApp

#### What does the project?
Well, the project is a map that allows follow track about a "user" through polyline building with an init and last point (
straight line)

#### why the project is usefull?
The project is usefull becouse accomplish with task previously with flexible architecture and technology 
  #### **Architecture and technology** 
  - MVVM architecture: in this case i omitted VM and repository
  - Dagger: as di framework
  - Navigation component: as new aproach to navigate between activities and fragments
  - Coroutines: As Kotlin aproach to build HTTP request in backgorund thread easily asnd safe
  - Clean code: As Software aproach to assign good names to variables, functions, code organization, etc..
  - SOLID principles: As support for previously to create code maintainable and scalable 
  
  ### Build project flow
  
- [x] Navigation component set up
- [x] Dagger set up for BaseApplication
- [x] BaseFragment
      - Request permissions
- [x] InitMapFragment
      - Subscribe this fragment in Dagger to provide instances
- [x] MapFragment
      -  Subscribe this fragment in Dagger to provide instances
      