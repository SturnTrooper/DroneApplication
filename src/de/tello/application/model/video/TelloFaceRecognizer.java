package de.tello.application.model.video;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

public class TelloFaceRecognizer {

    private CascadeClassifier faceClassifier;

    private int absoluteFaceSize;

    public TelloFaceRecognizer(){

        this.faceClassifier = new CascadeClassifier();
        this.absoluteFaceSize = 0;
        loadClassifier();
    }

    private void loadClassifier(){
        //faceClassifier.load("resources/FaceDetection/lbpcascade_frontalface.xml");
        faceClassifier.load("resources/FaceDetection/haarcascade_frontalface_alt.xml");
    }

    public void detectFaces(Mat pFrame){

        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        // convert the frame in gray scale
        Imgproc.cvtColor(pFrame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(grayFrame, grayFrame);

        // compute minimum face size (20% of the frame height, in our case)
        if (this.absoluteFaceSize == 0)
        {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0)
            {
                this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        // detect faces
        this.faceClassifier.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++)
            Imgproc.rectangle(pFrame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0), 3);


    }
}
