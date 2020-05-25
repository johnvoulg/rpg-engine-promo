/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicengine.character;

import java.util.ArrayList;

/**
 *
 * @author JVoulg
 */
public class Body {
    
    private BodyPart head;
    private BodyPart neck;
    private BodyPart upperBody;
    private BodyPart lowerBody;
    private BodyPart rightHand;
    private BodyPart leftHand;
    private BodyPart rightLeg;
    private BodyPart leftLeg;
    private ArrayList<BodyPart> bodyParts = new ArrayList<BodyPart>();
    
    Body() {
        this.head = new BodyPart("head", 50, true, true, 5, 3);
        this.neck = new BodyPart("neck", 50, true, false);
        this.upperBody = new BodyPart("upper body", 100, true, false);
        this.lowerBody = new BodyPart("lower body", 70, false, false);
        this.rightHand = new BodyPart("right hand", 50, false, true, 10, 5);
        this.leftHand = new BodyPart("left hand", 50, false, true, 10, 5);
        this.rightLeg = new BodyPart("right leg", 50, false, false, 15, 5);
        this.leftLeg = new BodyPart("left leg", 50, false, false, 15, 5);
        
        bodyParts.add(head);
        bodyParts.add(neck);
        bodyParts.add(upperBody);
        bodyParts.add(lowerBody);
        bodyParts.add(rightHand);
        bodyParts.add(leftHand);
        bodyParts.add(rightLeg);
        bodyParts.add(leftLeg);
    }
    
    public ArrayList<BodyPart> getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(ArrayList<BodyPart> bodyParts) {
        this.bodyParts = bodyParts;
    }
    
    public BodyPart getBodyPartByName(String name) {
        BodyPart bodyPart = null;
        for (BodyPart part : this.bodyParts) {
            if (part.getName().equals(name)) {
                bodyPart = part;
                break;
            }
        }
        return bodyPart;
    }
    
    public BodyPart getUpperBody() {
        return upperBody;
    }

    public void setUpperBody(BodyPart upperBody) {
        this.upperBody = upperBody;
    }

    public BodyPart getLowerBody() {
        return lowerBody;
    }

    public void setLowerBody(BodyPart lowerBody) {
        this.lowerBody = lowerBody;
    }

    public BodyPart getHead() {
        return head;
    }

    public void setHead(BodyPart head) {
        this.head = head;
    }

    public BodyPart getLeftHand() {
        return leftHand;
    }

    public void setLeftHand(BodyPart leftHand) {
        this.leftHand = leftHand;
    }

    public BodyPart getLeftLeg() {
        return leftLeg;
    }

    public void setLeftLeg(BodyPart leftLeg) {
        this.leftLeg = leftLeg;
    }

    public BodyPart getNeck() {
        return neck;
    }

    public void setNeck(BodyPart neck) {
        this.neck = neck;
    }

    public BodyPart getRightHand() {
        return rightHand;
    }

    public void setRightHand(BodyPart rightHand) {
        this.rightHand = rightHand;
    }

    public BodyPart getRightLeg() {
        return rightLeg;
    }

    public void setRightLeg(BodyPart rightLeg) {
        this.rightLeg = rightLeg;
    }
}