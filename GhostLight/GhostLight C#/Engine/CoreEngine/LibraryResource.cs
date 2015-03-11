//Author: Michael Letter

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CustomWindower.CoreEngine{
    /// <summary>
    /// Stores and Manages the loading of a single asset
    /// </summary>
    public abstract class LibraryResource {
        /// <summary> Pre made engine resource types have enums </summary>
        public enum ResourceType {SPRITE, FONT, SOUND, UNKOWN};

        private static ResourceType[] allResourceTypes = { ResourceType.SPRITE, ResourceType.FONT, ResourceType.SOUND };

        /// <summary> Stores the ID this object that will by used to hash this item 
        /// in a library. Note this ID must be unique to be put into a library</summary>
        private string id = "";
        /// <summary> Marks what type of resource this is </summary>
        private int type = -1;

        /// <summary>
        /// Will take a given int type and convert it to the coorisponding
        /// Pre made engine resource type enum if possible
        /// </summary>
        /// <param name="type">type to convert</param>
        public static ResourceType convertResourceType(int type){
            ResourceType retVal = ResourceType.UNKOWN;
            if (type > 0 && type < allResourceTypes.Length) {
                return allResourceTypes[type];
            }
            return retVal;
        }
        /// <summary>
        /// Will take a given int type and convert it to the coorisponding
        /// Pre made engine resource type enum if possible
        /// </summary>
        /// <param name="type">type to convert</param>
        public static int convertResourceType(ResourceType type){
            for (int loopSearch = 0; loopSearch < allResourceTypes.Length; loopSearch++) {
                if(allResourceTypes[loopSearch].Equals(type)){
                    return loopSearch;
                }
            }
            return -1;
        }
        /// <summary>
        /// Instantiates the loadableObject with the given ID and type. Note this is the only
        /// point where the ID and type can be set. These values can not be reset once this object is instantiated
        /// </summary>
        /// <param name="id">The UniqueID this resource will be associated with</param>
        /// <param name="type">The Type of resouce this library Resource is</param>
        public LibraryResource(string ID, int type){
            if(id != null){
                this.id = ID;
            }
        }
        /// <summary>
        /// Will return the ID this loadable object is associated with it
        /// </summary>
        /// <returns>the ID this loadable object is associated with it</returns>
        public string getID() {
            return id;
        }
        /// <summary>
        /// Will return the type of resource this is marked as
        /// </summary>
        /// <returns>the Type of resources this is marked as</returns>
        public int getType() {
            return type;
        }
        /// <summary>
        /// Returns whether or not this Resource has marked itself as loaded
        /// </summary>
        /// <returns>If True, the resource is , If false the has is not loaded</returns>
        public abstract bool isLoaded();
        /// <summary>
        /// Needs to conduct all of the computation and reads required to load this reasource
        /// and then update the bool loaded based on whether or not the load was successfull
        /// </summary>
        public abstract void loadResource();

        /// <summary>
        /// Needs dispose of and destroy any resouces that need be destroyed prior to the
        /// destruction of this class
        /// </summary>
        public abstract void unloadResource();
    }
}
