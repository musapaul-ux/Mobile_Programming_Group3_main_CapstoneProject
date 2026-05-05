# Mobile_Programming_Group3_main_CapstoneProject
Mobile programming final group project

App Name: Hostel Fix

TEAM MEMBERS AND THERE ROLES
1. Nakubulwa Winfred: 
       Lead Developer
       UI/UX Specialist
   
2. Alex Musa Paulino Longworong:
       Git and Quality Manager
       Documentation and Research Lead
   
3. Ayebare Cynthia
      Testing and Quality Assurance Engineer

FEATURE SET
  1. Role-Based Access Control:
        Secure authentication system with distinct interfaces and permissions for Students and Administrators.
  2. Comprehensive Complaint Management: 
         Students can submit maintenance requests including details such as Hostel Name, Room Number, Title, and Description.
  3. Administrative Oversight: 
        Admins can view all global complaints, update resolution statuses (Pending, In Progress, Resolved), and manage the user database.
  4. User Management (CRUD): 
        Dedicated admin panel to add, edit, and delete student or administrator accounts.
  5. Personalized Profiles:
        Users can manage their identity, update personal information, and upload/persist profile pictures from the device gallery.
  6. Immersive Onboarding: 
        Animated, full-screen Welcome Page featuring a cross-fading hostel imagery slideshow.
  7. Adaptive UI: 
        Full support for Dark and Light modes with a persistent toggle for user preference.
  8. Data Integrity & Validation: 
        Input guards ensuring numeric-only room numbers and mandatory field completion for all forms.

TECHNICAL STACK:
  1. Language:
        Kotlin 2.2.10 (Modern, expressive, and type-safe).
  2.UI Framework:
        Jetpack Compose with Material Design 3 for a reactive and modern user interface.
  3.Architecture:
        MVVM (Model-View-ViewModel) with the Repository Pattern to ensure clean separation of concerns and scalability.
  4.Local Database:
        Room Persistence Library for structured data storage of Users, Rooms, and Complaints.
  5.Navigation:
        Navigation Compose for handling role-based screen transitions and deep-linking.
  6.Image Handling:
       Coil (Coroutine Image Loader) for asynchronous loading and caching of user profile pictures.
  7.Data Persistence:
       DataStore Preferences for saving app-wide settings like theme preferences.
  8.Asynchronous Logic:
       Kotlin Coroutines and Flow for non-blocking database operations and reactive UI state updates.
  9.Dependency Management:
       Manual Dependency Injection facilitated through a custom AppViewModelProvider.
     
QA SUMMARY
This section presents a summary of testing carried out on the Hostel Fix application. The aim was to ensure that all major features such as authentication, complaint handling, navigation, and system stability work correctly and provide a smooth user experience.

 Feature Tested   	    Description    	                 Expected Result	                 Actual Result	                Status
User Registration	      New student creates an account	 Account created successfully	     Account created successfully	   Pass
User Login	            Login with valid credentials	   Redirect to dashboard	           Dashboard displayed	           Pass
User Login (Invalid)	  Login with wrong credentials	   Error message displayed	         Error message shown	           Pass
Submit Complaint	      Student submits complaint	       Complaint saved in system	       Complaint saved	               Pass
View Complaints	        User views complaints	           Complaints list displayed	       Displayed correctly	           Pass
Complaint Status Update	Admin updates                    complaint	Status updated	       Updated successfully	           Pass
Navigation	            User taps home button	           Go to home screen	               Navigation works	               Pass
Profile View	          User views profile	             Profile info displayed	           Displayed correctly	           Pass
UI Stability	          Navigate screens	               No flicker/crash	                 Stable	                         Pass
Logout	                User logs out	                   Redirect to login	               Logout successful	             Pass

Test Summary
All test cases passed successfully, giving a 100% success rate. This confirms that the Hostel Fix system is stable, reliable, and ready for deployment. No critical issues were identified during testing.

                      
