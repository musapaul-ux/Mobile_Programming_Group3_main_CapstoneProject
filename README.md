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
This section summarizes the testing conducted on the Hostel Fix application to verify the functionality of key features, including user authentication, complaint management, navigation, session handling, and system stability.
Testing confirmed that user registration and login processes function correctly, with valid credentials granting access and invalid attempts producing appropriate error messages. The complaint management module performed as expected, allowing users to submit and view complaints, while administrators successfully updated complaint statuses.

System navigation, including the home and profile features, operated correctly. Session management was reliable, maintaining user login states appropriately. The system also handled network interruptions effectively by displaying relevant error messages. Additionally, the user interface remained stable during use, with no observed crashes or flickering.
All test cases passed successfully, achieving a 100% success rate. The system is therefore considered stable, reliable, and ready for deployment.
                      
