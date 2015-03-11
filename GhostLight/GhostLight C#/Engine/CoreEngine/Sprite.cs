//Author: Michael Letter

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Drawing.Drawing2D;

namespace CustomWindower.CoreEngine {
    /// <summary>
    /// Manages the loading and storiage of an animated sprite from file
    /// </summary>
    public class Sprite : LibraryResource{
        /// <summary> The number of rows of frames in each image</summary>
        private int rows = 1;
        /// <summary> The number of collumns of frames in each image</summary>
        private int collumns = 1;
        /// <summary> The number of missing images on the last row of the last image </summary>
        private int offset = 0;
        /// <summary> The total number of frames in the Sprite</summary>
        private int totalFrames = 1;
        /// <summary> All of the images that make up the spriteSheet </summary>
        protected System.Drawing.Image[] spriteSheets = null;
        /// <summary> The File Locations of all of the images that compose this image</summary>
        protected String[] imageFileLocations = null;
        /// <summary> Used to Descibe the positon and Scale of the image when Drawn  </summary>
        private System.Drawing.RectangleF positionScale = new System.Drawing.RectangleF();
        /// <summary> Used to Desctibe the position and size of single Frame </summary>
        private System.Drawing.RectangleF frameLocation = new System.Drawing.RectangleF();
        /// <summary> Each Draw the Requires the Use of a Matrix, This is it </summary>
        private Matrix tempMatrix = new Matrix();

        /// <summary>
        /// Will create a sprite with 1 frame from the image at the given fileLocation when loadResource is called
        /// Note, fileLocation will also be used as this resources ID
        /// </summary>
        /// <param name="fileLocation">the location of the image file that will be loaded</param>
        public Sprite(String fileLocation) : base(fileLocation, LibraryResource.convertResourceType(ResourceType.SPRITE)) {
            if(fileLocation != null){
                imageFileLocations = new String[1];
                imageFileLocations[0] = fileLocation;
            }
            this.rows = 1;
            this.collumns = 1;
            this.offset = 0;
            totalFrames = (rows * collumns * imageFileLocations.Length) - offset;
        }
        /// <summary>
        /// Will create a sprite with multiple frames from the image at the given fileLocation when loadResource is called
        /// Note, fileLocation will also be used as this resources ID
        /// </summary>
        /// <param name="fileLocation">the location of the image file that will be loaded</param>
        /// <param name="rows">The number of images in a row that are contained in the image File</param>
        public Sprite(String fileLocation, int rows) : base(fileLocation, LibraryResource.convertResourceType(ResourceType.SPRITE)) {
            if(fileLocation != null){
                imageFileLocations = new String[1];
                imageFileLocations[0] = fileLocation;
            }
            if (rows > 0) {
                this.rows = rows;
            }
            this.collumns = 1;
            this.offset = 0;
            totalFrames = (rows * collumns * imageFileLocations.Length) - offset;
        }
        /// <summary>
        /// Will create a sprite with multiple frames from the image at the given fileLocation when loadResource is called
        /// Note, fileLocation will also be used as this resources ID
        /// </summary>
        /// <param name="fileLocation">the location of the image file that will be loaded</param>
        /// <param name="rows">The number of images in each row in the image File</param>
        /// <param name="collumns">The number of images in each collumn in the image File</param>
        public Sprite(String fileLocation, int rows, int collumns) : base(fileLocation, LibraryResource.convertResourceType(ResourceType.SPRITE)) {
            if(fileLocation != null){
                imageFileLocations = new String[1];
                imageFileLocations[0] = fileLocation;
            }
            if (rows > 0){
                this.rows = rows;
            }
            if (collumns > 0){
                this.collumns = collumns;
            }
            this.offset = 0;
            totalFrames = (rows * collumns * imageFileLocations.Length) - offset;
        }
        /// <summary>
        /// Will create a sprite with multiple frames from the image at the given fileLocation when loadResource is called
        /// Note, fileLocation will also be used as this resources ID
        /// </summary>
        /// <param name="fileLocation">the location of the image file that will be loaded</param>
        /// <param name="rows">The number of images in each row in the image File</param>
        /// <param name="collumns">The number of images in each collumn in the image File</param>
        /// <param name="offset">The number of frames that are missing from the last row of image.
        /// Note, It is assumed that the existing images are right oriented</param>
        public Sprite(String fileLocation, int rows, int collumns, int offset) : base(fileLocation, LibraryResource.convertResourceType(ResourceType.SPRITE)) {
            if(fileLocation != null){
                imageFileLocations = new String[1];
                imageFileLocations[0] = fileLocation;
            }
            if (rows > 0){
                this.rows = rows;
            }
            if (collumns > 0){
                this.collumns = collumns;
            }
            if(offset > 0){
                this.offset = offset;
            }
            totalFrames = (rows * collumns * imageFileLocations.Length) - offset;
        }
        /// <summary>
        /// Will create a sprite with multiple frames from the image at the given fileLocation when loadResource is called
        /// Note, fileLocation[0] will also be used as this resources ID
        /// </summary>
        /// <param name="fileLocation">The Locations of all of the images to be used by this sprite
        /// Note, all of the images must be the same size for them to be dispayed properly</param>
        public Sprite(String[] fileLocation) : base(fileLocation[0], LibraryResource.convertResourceType(ResourceType.SPRITE)){
            if(fileLocation != null){
                imageFileLocations = new String[fileLocation.Length];
                for (int loop = 0; loop < imageFileLocations.Length; loop++) {
                    if(fileLocation[loop] != null){
                        imageFileLocations[loop] = fileLocation[loop];
                    }
                    else{
                        imageFileLocations[loop] = null;
                    }
                }
            }
            this.rows = 1;
            this.collumns = 1;
            this.offset = 0;
            totalFrames = (rows * collumns * imageFileLocations.Length) - offset;
        }
        /// <summary>
        /// Will create a sprite with multiple frames from the image at the given fileLocation when loadResource is called
        /// Note, fileLocation[0] will also be used as this resources ID
        /// </summary>
        /// <param name="fileLocation">The Locations of all of the images to be used by this sprite
        /// Note, all of the images must be the same size for them to be dispayed properly</param>
        /// <param name="rows">The number of images in a row that are contained in the image File</param>
        public Sprite(String[] fileLocation, int rows) : base(fileLocation[0], LibraryResource.convertResourceType(ResourceType.SPRITE)) {
            if(fileLocation != null){
                imageFileLocations = new String[fileLocation.Length];
                for (int loop = 0; loop < imageFileLocations.Length; loop++) {
                    if(fileLocation[loop] != null){
                        imageFileLocations[loop] = fileLocation[loop];
                    }
                    else{
                        imageFileLocations[loop] = null;
                    }
                }
            }
            if (rows > 0){
                this.rows = rows;
            }
            this.collumns = 1;
            this.offset = 0;
            totalFrames = (rows * collumns * imageFileLocations.Length) - offset;
        }
        /// <summary>
        // Will create a sprite with multiple frames from the image at the given fileLocation when loadResource is called
        /// Note, fileLocation[0] will also be used as this resources ID
        /// </summary>
        /// <param name="fileLocation">The Locations of all of the images to be used by this sprite
        /// Note, all of the images must be the same size for them to be dispayed properly</param>
        /// <param name="rows">The number of images in each row in the image File</param>
        /// <param name="collumns">The number of images in each collumn in the image File</param>
        public Sprite(String[] fileLocation, int rows, int collumns) : base(fileLocation[0], LibraryResource.convertResourceType(ResourceType.SPRITE)) {
            if(fileLocation != null){
                imageFileLocations = new String[fileLocation.Length];
                for (int loop = 0; loop < imageFileLocations.Length; loop++) {
                    if(fileLocation[loop] != null){
                        imageFileLocations[loop] = fileLocation[loop];
                    }
                    else{
                        imageFileLocations[loop] = null;
                    }
                }
            }
            if (rows > 0){
                this.rows = rows;
            }
            if (collumns > 0){
                this.collumns = collumns;
            }
            this.offset = 0;
            totalFrames = (rows * collumns * imageFileLocations.Length) - offset;
        }
        /// <summary>
        // Will create a sprite with multiple frames from the image at the given fileLocation when loadResource is called
        /// Note, fileLocation[0] will also be used as this resources ID
        /// </summary>
        /// <param name="fileLocation">The Locations of all of the images to be used by this sprite. 
        /// Note all of the images must be the same size for them to be dispayed properly</param>
        /// <param name="rows">The number of images in each row in the image File</param>
        /// <param name="collumns">The number of images in each collumn in the image File</param>
        /// <param name="offset">The number of frames that are missing from the last row of image.
        /// Note, It is assumed that the existing images are right oriented</param>
        public Sprite(String[] fileLocation, int rows, int collumns, int offset) : base(fileLocation[0], LibraryResource.convertResourceType(ResourceType.SPRITE)) {
            if(fileLocation != null){
                imageFileLocations = new String[fileLocation.Length];
                for (int loop = 0; loop < imageFileLocations.Length; loop++) {
                    if(fileLocation[loop] != null){
                        imageFileLocations[loop] = fileLocation[loop];
                    }
                    else{
                        imageFileLocations[loop] = null;
                    }
                }
            }
            if (rows > 0){
                this.rows = rows;
            }
            if (collumns > 0){
                this.collumns = collumns;
            }
            if(offset > 0){
                this.offset = offset;
            }
            totalFrames = (rows * collumns * imageFileLocations.Length) - offset;
        }
        /// <summary>
        /// Returns the Total number of frames in the Sprite
        /// </summary>
        /// <returns>the Total number of frames in the Sprite</returns>
        public int getTotalFrames(){
            return totalFrames;
        }
        /// <summary>
        /// Will Draw this image to in the given camera
        /// </summary>
        /// <param name="painLocation">The Camera this Sprite will be drawn in</param>
        /// <param name="frame">Determines the Frame of the Sprite that will be drawn, Note if the given fram index does not exist this function will fail</param>
        /// <param name="Transformation">Determines the Location, Scale, Rotation and Shere of the image, Note The Image will alwats be scaled up from size 1x1 and its center will always be at (0,0) So if you want to see the image scale it up</param>
        /// <returns>if True, the  sprite was drawn successfully, ontherwise returns false</returns>
        public bool paint(Camera painLocation, int frame, System.Drawing.Drawing2D.Matrix Transformation) {
            if(isLoaded() && frame >= 0 && frame < totalFrames && painLocation != null && Transformation != null){
                tempMatrix.Reset();
                tempMatrix.Scale((float)(1f/positionScale.Width), (float)(1f/positionScale.Height), MatrixOrder.Append);
                tempMatrix.Multiply(Transformation, MatrixOrder.Append);
                frameLocation.X = frame % collumns * frameLocation.Width;
                frameLocation.Y = (frame / collumns) % rows * frameLocation.Height;
                return painLocation.paintImage(spriteSheets[(frame / (collumns * rows)) % spriteSheets.Length], frameLocation, positionScale, tempMatrix);
            }
            return false;
        }
        /// <summary>
        /// returns whether or not this Sprite has loaded successfully
        /// </summary>
        /// <returns>If True all of the images required for this sprite loaded successfully, If False, one or more of the images involved failed to load</returns>
        public override bool isLoaded() {
            return spriteSheets != null;
        }
        /// <summary>
        /// returns true if a given image loaded successfully
        /// </summary>
        /// <param name="image">the index of the image in question if, if only one image location was given the only image index is 0
        /// if an array of image locations was given then image maps to the index of the image in question</param>
        /// <returns></returns>
        public bool isLoaded(int image){
            return (spriteSheets != null && image < spriteSheets.Length && image >= 0 && spriteSheets[image] != null);
        }
        /// <summary>
        /// Will load all of the images associated with this sprite
        /// </summary>
        public override void loadResource() {
            //unloaded existing images
            if(isLoaded()){
                unloadResource();
            }
            //loading new images
            if(imageFileLocations != null && imageFileLocations.Length > 0){
                bool successfullLoad = false;
                spriteSheets = new Image[imageFileLocations.Length];
                for(int loop = 0; loop < imageFileLocations.Length; loop++){
                    //Attempt to load a single image
                    try{
                        //Success
                        spriteSheets[loop] = Image.FromFile(imageFileLocations[loop]);
                        successfullLoad = true;
                    }
                        //Faliure
                    catch(System.IO.FileNotFoundException e){
                        Console.Error.WriteLine("Sprite Error Loading Image[" + loop + "]" + e.Message);
                        spriteSheets[loop] = null;
                    }
                    catch(System.OutOfMemoryException e){
                        Console.Error.WriteLine("Sprite Error Loading Image[" + loop + "]" + e.Message);
                        spriteSheets[loop] = null;
                    }
                    catch(System.ArgumentException e){
                        Console.Error.WriteLine("Sprite Error Loading Image[" + loop + "]" + e.Message);
                        spriteSheets[loop] = null;
                    }
                }
                //If no images loaded successfully destroying array
                if(!successfullLoad){
                    spriteSheets = null;
                }
                //Initializing Frame Size, image Scale
                else{
                    positionScale.Width = (float)spriteSheets[0].Width / collumns;
                    positionScale.Height = (float)spriteSheets[0].Height / rows;
                    positionScale.X = -positionScale.Width/2;
                    positionScale.Y = -positionScale.Height/2;
                    frameLocation.Width = positionScale.Width;
                    frameLocation.Height = positionScale.Height;
                }
            }
        }
        /// <summary>
        /// Will dispose of all of the resources used by this image
        /// </summary>
        public override void unloadResource() {
            if(spriteSheets != null){
                for (int loop = 0; loop < spriteSheets.Length; loop++) {
                    if(spriteSheets[loop] != null){
                        spriteSheets[loop].Dispose();
                        spriteSheets[loop] = null;
                    }
                }
                spriteSheets = null;
            }
        }
        /// <summary>
        /// Will return the index of the frame at the given location in the sprite Sheet(s)
        /// </summary>
        /// <param name="rowLocation">The index of the row the frame resides in</param>
        /// <param name="collumnlocation">the collumn index of the collumn the frame resides in frame </param>
        /// <param name="containingimage">the image index of the target frame(if only one image was loaded use 0)</param>
        /// <returns>the index of the frame at the given location. If negative then the target Frame does not exist</returns>
        public int getFrameIndex(int rowLocation, int collumnlocation, int containingimage) {
            int retVal = -1;
            if(rowLocation >= 0 && rowLocation < rows 
                    && collumnlocation >= 0 && collumnlocation < collumns
                    && containingimage >= 0 && imageFileLocations != null && containingimage < imageFileLocations.Length){
                    retVal = rowLocation + (collumns * collumnlocation) + (collumns * rows * containingimage);
                if (retVal >= totalFrames) {
                    retVal = -1;
                }
            }
            return retVal;
        }
    }
}
