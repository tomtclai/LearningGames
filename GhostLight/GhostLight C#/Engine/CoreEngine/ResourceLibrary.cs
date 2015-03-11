//Author: Michael Letter

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CustomWindower.CoreEngine {
    /// <summary>
    /// manages the loading and storage of the current assets
    /// </summary>
    public class ResourceLibrary {

        /// <summary> Stores the mapping between library Resources and there ID's </summary>
        private Dictionary<string, LibraryResource> library = new Dictionary<string, LibraryResource>();
        /// <summary> Stores all of the library Resources for quick linear seach</summary>
        private LinkedList<LibraryResource> LibraryCollection = new LinkedList<LibraryResource>();

        /// <summary>
        /// Will set class variables to defualt values
        /// </summary>
        public ResourceLibrary(){
            //Uhhh...
        }
        /// <summary>
        /// Will add the given resource to the library using its ID as the 
        ///Key that can be used to access it with. Note if another resource in the library 
        ///has the same ID as the given resource this resource can not be added
        /// </summary>
        /// <param name="newResource">The Resource to be added</param>
        /// <returns>If True the resource was successfully added, if False, no changes were made</returns>
        public bool addResource(LibraryResource newResource) {
            if (newResource != null && !library.ContainsKey(newResource.getID())) {
                library.Add(newResource.getID(), newResource);
                LibraryCollection.AddFirst(newResource);
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will Return the library resource with the given key in the library
        /// </summary>
        /// <param name="resourceID">the ID of the target resource</param>
        /// <returns>the library resource with the given ID. if NULL, 
        /// then no resource was found with the given ID</returns>
        public LibraryResource getResource(string resourceID) {
            if(resourceID != null && library.ContainsKey(resourceID)){
               return library[resourceID]; 
            }
            return null;
        }
        /// <summary>
        /// Will remove the library Resource with the given ID from the library if one exists
        /// </summary>
        /// <param name="resourceID">the ID of the targetResource</param>
        /// <returns>If True the resource was successfully added, if False, no changes were made</returns>
        public bool addRemoveResource(string resourceID) {
            if(resourceID != null && library.ContainsKey(resourceID)){
                LibraryResource removed = library[resourceID];
                library.Remove(resourceID);
                LibraryCollection.Remove(removed);
                return true;
            }
            return false;
        }
        /// <summary>
        /// Will call loadResource() on all of the resources in the library
        /// </summary>
        /// <param name="ignoreLoaded">If True LibraryResources that have marked themselves as loaded will be ignored.
        /// If False, all LibraryResources will be loaded </param>
        public void loadAll(bool ignoreLoadedResources) {
            foreach (LibraryResource resource in LibraryCollection){
                if(!resource.isLoaded()){
                    resource.loadResource();
                }
                else if (!ignoreLoadedResources) {
                    resource.unloadResource();
                    resource.loadResource();
                }
            }
        }
        /// <summary>
        /// Will call unloadResource 
        /// </summary>
        ///  /// <param name="ignoreLoaded">If True LibraryResources that have marked themselves as unloaded will be ignored.
        /// If False, all LibraryResources will be unloaded </param>
        public void unloadAll(bool ignoreUnoadedResources) {
            foreach (LibraryResource resource in LibraryCollection){
                if(resource.isLoaded() || !ignoreUnoadedResources){
                    resource.loadResource();
                }
            }
        }
    }
}
