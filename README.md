BR Continuity - Android
============

### Purpose
This library provides a means to retain an object across Android lifecycle events for a specified amount of time after that object has been destroyed. The objects are keyed on the type of the "anchor" object requesting it, the Android Task that the anchor exists in, the type of the continuous class, and an optional string tag. 

### Key Concepts
* Anchor - An object whose existence is used to determine whether to keep the ContinuousObject around.
* ContinuousObject - Any object that can optionally implement the ContinuousObject interface which you want to keep around across lifecycle events. 
* Tag - An optional arbitrary string that will generate a different ContainerId. 
* Android Task - The Android system keeps stacks of Activities in Tasks. For multi-window support two Activities of identical type could be visible at once belonging to two separate Tasks. 
* ContainerId - A combination of the canonical name of the Anchor, Android Task ID, canonical name of the ContinuousObject, and a Tag which uniquely identifies a ContinuousObject for storage and retrieval.
* Check Interval - Time in milliseconds between freshness checks to determine if an anchor object has been garbage collected. 
* Default Lifetime - Time in milliseconds to retain a ContinuousObject after all associated Anchor objects have been garbage collected. 
* Idle Shutdown - Time in milliseconds that the freshness check thread will run without any objects being accessed. After this time the thread will shut down until the next operation occurs. 
* Max Empty Iterations - If the repository is empty for this number of iterations, the freshness checking thread will shutdown until future operations occur. 
* ContinuityFactory - Generic Functional Interface that will create an object that requires constructor parameters.

### Usage
The ContinuityRepository object should be held in such a way that there is only one instance in use and that it outlives your Activities. You can use a Singleton to contain it, a DI framework, Service Locator, or whatever floats your boat. In the examples we'll just assume I have this Singleton-like instance at hand already.

#### Presenter
Create a presenter class somewhere and be sure it implements ContinuousObject if you want to be notified when it is being discarded. 

		public class MyPresenter implements ContinuousObject {
					
			//Do all the things you need to do in your presenter.
			
			@Override
			onContinuityDiscard() {
				//Disconnect anything you are observing, cancel operations, etc.
			}
		}

#### Activity
Now in your Activity whenever you are ready to get the object.

		@Override
		onCreate(@Nullable Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//Inflate layouts, etc.
			
			//Use this Activity as the Anchor, and create or fetch an instance of MyPresenter
			MyPresenter myPresenter = continuityRepository.with(this, MyPresenter.class).build();
		}

That is it! The first time the Activity is created for a specific Android Task you will get a new instance. From that point onward until the Activity has been garbage collected for a specified amount of time, you will get the same MyPresenter Object.

#### Fragment
Fragments are a slightly different case. Because the Android Task ID is unknown until it is attached to the Activity, you have to use the onActivityCreated method to properly identify the Android Task. 

		@Override
		public void onActivityCreated(@Nullable Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        //Do anything else you need to do here.

			//Use this Fragment as the Anchor, and create or fetch an instance of MyPresenter
			MyPresenter myPresenter = continuityRepository.with(this, MyPresenter.class).build();
	    }
		
**NOTE:** This will not be the same instance that was provided to the Activity in the example above even if the Fragment is attached to that Activity. If you want them to receive the same Presenter(not recommended) pass the Activity instance as the Anchor for both calls. Seriously, you almost certainly shouldn't do this but there it is.

#### Changing Defaults
If the default timeouts are not acceptable, you can specify your own in the ContinuityRepository constructor. You can also override the lifetime of a single ContinuousObject by specifying a different lifetime in the builder. 

#### Builder Options
		
		final String testString = "test"; //Used later for the ExampleTestClass constructor.
		
		ExampleTestClass exampleTestClass = continuityRepository.with(this, ExampleTestClass.class)
			.task(1234)     // Provide an arbitrary task id instead of trying to obtain it from the Anchor
		    .lifetime(1000) // Change the lifetime that this ContinuousObject should be retained
		    .using(new ContinuityFactory<ExampleTestClass>() {
               @Override
                public ExampleTestClass create() {
                    return new ExampleTestClass(testString);
                }
            })              //Use an anonymous ContinuityFactory if the constructor requires arguments.
            .tag("example1") //Further differentiate the ContainerId with a custom tag like an article Id.
            .build();       //Either create or retrieve the ContinuousObject instance.

In most cases the ContinuityFactory should be avoided in favor of just calling accessor methods on the ContinuousObject instance you are creating. That ensures that if a cached object were returned from the repository, it has the correct information. If the parameters passed to the constructor will never change and really need to be final in the ContinuousObject, then you have a good case to use it.

The tag can be used to be sure you get a fresh presenter if the same Android Task and Fragment/Activity are displaying a different item in ViewPager for example. 

#### Manual Removal
If you really need to ensure that an instance of a ContinuousObject is no longer referenced and do not want to wait for garbage collection and timeout to occur, you can manually remove the object. If you are doing this a lot, then there isn't much benefit over manually managing the objects yourself. 

		//For the 99% case
		continuousRepository.with(this, MyPresenter.class).remove();

		//Or for the Builder Options example above. 
		continuousRepository.with(this, ExampleTestClass.class).task(1234).tag("example1").remove();

Any other optional attributes you used to distinguish the ContinuousId in the original builder like a different task or tag must be provided as well. You do not need to provide the ContinuityFactory even if you used one to create the original object, it will never be called in a removal and is irrelevant. 

#### Sample Application
Look in this repository for a sample application that demonstrates the use of this library along with the MVVMP design pattern enabled by the release of Android DataBinding. 

### Gradle
Add the jcenter repository and include the library in your project with the compile directive in your dependencies section of your build.gradle.

        repositories {
            ...
            jcenter()
        }
        
        ...

        dependencies {
            ...
            compile 'com.bottlerocketstudios:continuity:1.0.0'
        }

In rare cases where you need to pull a snapshot build to help troubleshoot the develop branch, snapshots are hosted by JFrog. You should not ship a release using the snapshot library as the actual binary referenced by snapshot is going to change with every build of the develop branch. In the best case you will have irreproducible builds. In the worst case, human extinction. In some more likely middle case, you will have buggy or experimental code in your released app.

         repositories {
            ...
            jcenter()
            maven {
               url "https://oss.jfrog.org/artifactory/oss-snapshot-local"
            }
         }
         
         dependencies {
            ...
            compile 'com.bottlerocketstudios:continuity:1.1.0-SNAPSHOT'
         }

### Build
This project must be built with gradle. 

*   Version Numbering - The version name should end with "-SNAPSHOT" for non release builds. This will cause the resulting binary, source, and javadoc files to be uploaded to the snapshot repository in Maven as a snapshot build. Removing snapshot from the version name will publish the build on jcenter. If that version is already published, it will not overwrite it.
*   Execution - To build this libarary, associated tasks are dynamically generated by Android build tools in conjunction with Gradle. Example command for the production flavor of the release build type: 
    *   Build and upload: `./gradlew --refresh-dependencies clean uploadToMaven`
    *   Build only: `./gradlew --refresh-dependencies clean jarRelease`
