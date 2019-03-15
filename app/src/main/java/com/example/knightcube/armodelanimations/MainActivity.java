package com.example.knightcube.armodelanimations;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.SkeletonNode;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;


public class MainActivity extends AppCompatActivity {

    Button animateBtn;
    ModelAnimator modelAnimator;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animateBtn = findViewById(R.id.animate_btn);
        ArFragment arFragment = (ArFragment)getSupportFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            placeModel(hitResult.createAnchor(),arFragment);
        });

    }

    private void placeModel(Anchor anchor, ArFragment arFragment) {
        ModelRenderable.builder()
                .setSource(this,Uri.parse("ferris_wheel.sfb"))
                .build()
                .thenAccept(modelRenderable -> {
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    SkeletonNode skeletonNode = new SkeletonNode();
                    skeletonNode.setParent(anchorNode);
                    skeletonNode.setRenderable(modelRenderable);
                    arFragment.getArSceneView().getScene().addChild(anchorNode);
                    animateBtn.setOnClickListener(v -> {animateModel(modelRenderable);});
                });
    }

    private void animateModel(ModelRenderable modelRenderable) {

        if((modelAnimator!=null) &&(modelAnimator.isRunning())){
            modelAnimator.end();
        }
        int animationCount = modelRenderable.getAnimationDataCount();

        if(i==animationCount)i=0;

        AnimationData animationData = modelRenderable.getAnimationData(i);
        modelAnimator = new ModelAnimator(animationData,modelRenderable);
        modelAnimator.start();
        i++;
    }
}
