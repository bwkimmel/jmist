/**
 * Java Modular Image Synthesis Toolkit (JMIST)
 * Copyright (C) 2008-2013 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package ca.eandb.jmist.framework.loader.dxf;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.jmist.framework.accel.BoundingIntervalHierarchy;
import ca.eandb.jmist.framework.color.ColorModel;
import ca.eandb.jmist.framework.color.rgb.RGBColorModel;
import ca.eandb.jmist.framework.geometry.primitive.PolyhedronGeometry;
import ca.eandb.jmist.framework.material.LambertianMaterial;
import ca.eandb.jmist.framework.scene.CollapseSceneElement;
import ca.eandb.jmist.framework.scene.MaterialSceneElement;
import ca.eandb.jmist.framework.scene.MergeSceneElement;
import ca.eandb.jmist.framework.scene.TransformableSceneElement;
import ca.eandb.jmist.math.AffineMatrix3;
import ca.eandb.jmist.math.Basis3;
import ca.eandb.jmist.math.MathUtil;
import ca.eandb.jmist.math.Point3;
import ca.eandb.jmist.math.Vector3;

/**
 * @author Brad
 *
 */
public final class DxfSceneBuilder {
  
  private static final int ACCEL_THRESHOLD = Integer.MAX_VALUE; 
  
  private static final class Block {
    public Point3 base;
    public MergeSceneElement geometry;
    public SceneElement root;
    public PolyhedronGeometry _3dfaces = null;
  }
  
  private static final class State {
    public String currentSection;
    
    public Map<String, Block> blocks = new HashMap<String, Block>();
    
    public Block currentBlock = null;
    public PolyhedronGeometry currentMesh = null;
    public MergeSceneElement root = new MergeSceneElement();
    public PolyhedronGeometry _3dfaces = null;
  }
  
  private static interface GroupHandler {
    
    void parse(State state, DxfReader dxf);
    
  }
  
  @SuppressWarnings("unused")
  static final class GroupHandler_BLOCK implements GroupHandler {

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.loader.dxf.SceneBuilder.GroupHandler#parse(ca.eandb.jmist.framework.loader.dxf.SceneBuilder.State, ca.eandb.jmist.framework.loader.dxf.DxfReader)
     */
    @Override
    public void parse(State state, DxfReader dxf) {
      int flags = 0;
      String name = null;
      String xref = null;
      double[] p = { 0.0, 0.0, 0.0 };
      
      DxfElement elem;
      int gc;
      
      do {
        dxf.advance();
        elem = dxf.getCurrentElement();
        gc = elem.getGroupCode();
        
        switch (gc) {
        
        case 1:
          xref = elem.getStringValue();
          break;
          
        case 2:
        case 3:
          name = elem.getStringValue();
          break;
          
        case 10:
        case 20:
        case 30:
          p[(gc - 10) / 10] = elem.getFloatValue();
          break;
          
        }
      } while (gc != 0);
      
      if (xref != null && !xref.isEmpty()) {
        System.err.println("Warning, Xrefs in blocks not supported");
        System.err.printf("Block xref: %s", xref);
        System.err.println();
      }
      
      state.currentBlock = new Block();
      state.currentBlock.base = new Point3(p[0], p[1], p[2]);
      state.currentBlock.geometry = new MergeSceneElement();
      
      if (state.currentBlock.base.squaredDistanceToOrigin() > MathUtil.EPSILON) {
        System.out.println("Block with non-origin base.");
      }
      
      state.blocks.put(name, state.currentBlock);
      
    }
    
  }
  
  static final class GroupHandler_ENDBLK implements GroupHandler {

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.loader.dxf.SceneBuilder.GroupHandler#parse(ca.eandb.jmist.framework.loader.dxf.SceneBuilder.State, ca.eandb.jmist.framework.loader.dxf.DxfReader)
     */
    @Override
    public void parse(State state, DxfReader dxf) {
      state.currentMesh = null;
      state.currentBlock = null;
      dxf.advance();
    }
    
  }
  
  static final class xGroupHandler_3DFACE implements GroupHandler {

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.loader.dxf.SceneBuilder.GroupHandler#parse(ca.eandb.jmist.framework.loader.dxf.SceneBuilder.State, ca.eandb.jmist.framework.loader.dxf.DxfReader)
     */
    @Override
    public void parse(State state, DxfReader dxf) {
      
      double[] v = {
          0.0, 0.0, 0.0,
          0.0, 0.0, 0.0,
          0.0, 0.0, 0.0,
          0.0, 0.0, 0.0
      };
      
      DxfElement elem;
      int gc;
      
      do {
        dxf.advance();
        elem = dxf.getCurrentElement();
        gc = elem.getGroupCode();
        
        switch (gc) {

        case 10:
        case 11:
        case 12:
        case 13:
        case 20:
        case 21:
        case 22:
        case 23:
        case 30:
        case 31:
        case 32:
        case 33:
          int vi = gc % 10;
          int ci = (gc - 10) / 10;
          v[3 * vi + ci] = elem.getFloatValue();
          break;
          
        }
      } while (gc != 0);
      
      PolyhedronGeometry mesh = null;
      
      if (state.currentSection.equals("ENTITIES")) {
        if (state._3dfaces == null) {
          state._3dfaces = new PolyhedronGeometry();
          state.root.addChild(state._3dfaces);
        }
        mesh = state._3dfaces;
      } else if (state.currentBlock != null) {
        if (state.currentBlock._3dfaces == null) {
          state.currentBlock._3dfaces = new PolyhedronGeometry();
          state.currentBlock.geometry.addChild(state.currentBlock._3dfaces);
        }
        mesh = state.currentBlock._3dfaces;
      } else {
        return;
      }
      
      Point3[] vertices = new Point3[4];
      for (int i = 0; i < 4; i++) {
        vertices[i] = new Point3(v[3 * i + 0], v[3 * i + 1], v[3 * i + 2]);
      }
      
      int nv = vertices[3].squaredDistanceTo(vertices[2]) < MathUtil.EPSILON ? 3 : 4;
      int offset = mesh.getNumVertices();

      for (int i = 0; i < nv; i++) {
        mesh.addVertex(vertices[i]);
      }
      
      mesh.addFace(new int[]{ offset + 0, offset + 1, offset + 2 });
      if (nv > 3) { // quad
        mesh.addFace(new int[]{ offset + 2, offset + 3, offset + 0 });
      }
      
    }
    
  }
  
  @SuppressWarnings("unused")
  static final class GroupHandler_POLYLINE implements GroupHandler {

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.loader.dxf.SceneBuilder.GroupHandler#parse(ca.eandb.jmist.framework.loader.dxf.SceneBuilder.State, ca.eandb.jmist.framework.loader.dxf.DxfReader)
     */
    @Override
    public void parse(State state, DxfReader dxf) {
      int flags = 0;
      int surfaceType = 0;
      
      DxfElement elem;
      
      do {
        dxf.advance();
        elem = dxf.getCurrentElement();
        
        switch (elem.getGroupCode()) {
          
        case 70: // flags
          flags = elem.getIntegerValue();
          break;
          
        case 75: // surface type
          surfaceType = elem.getIntegerValue();
          break;
        }
      } while (elem.getGroupCode() != 0);
  
      if ((flags & 64) != 0) {
        state.currentMesh = new PolyhedronGeometry();
        if (state.currentBlock != null) {
          state.currentBlock.geometry.addChild(state.currentMesh);
        }
        if (state.currentSection.equals("ENTITIES")) {
          state.root.addChild(state.currentMesh);
        }
      }
    }
    
  }
  
  static final class GroupHandler_VERTEX implements GroupHandler {

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.loader.dxf.SceneBuilder.GroupHandler#parse(ca.eandb.jmist.framework.loader.dxf.SceneBuilder.State, ca.eandb.jmist.framework.loader.dxf.DxfReader)
     */
    @Override
    public void parse(State state, DxfReader dxf) {
      
      DxfElement elem;
      int flags = 0;
      double[] v = { 0.0, 0.0, 0.0 };
      int[] f = { 0, 0, 0, 0 };
      int gc;
      
      do {
        dxf.advance();
        elem = dxf.getCurrentElement();
        gc = elem.getGroupCode();
        
        switch (gc) {
        
        case 10:
        case 20:
        case 30:
          v[(gc - 10) / 10] = elem.getFloatValue();
          break;
        
        case 70: // flags
          flags = elem.getIntegerValue();
          
          /* all polyface vertices have 128-bit set.  If it is not
           * set, we are not interested in this vertex.
           */
          if ((flags & 128) == 0) {
            return;
          }
          break;

        case 71:
        case 72:
        case 73:
        case 74:
          f[gc - 71] = elem.getIntegerValue();
          break;
          
        }
      } while (gc != 0);
      
      if ((flags & 64) != 0) { // mesh vertex
        state.currentMesh.addVertex(new Point3(v[0], v[1], v[2]));
      } else { // mesh face
        if (f[2] > 0) { // we require at least 3 vertices
          state.currentMesh.addFace(new int[]{ f[0] - 1, f[1] - 1, f[2] - 1 });
          if (f[3] > 0) { // quad
            state.currentMesh.addFace(new int[]{ f[2] - 1, f[3] - 1, f[0] - 1 });
          }
        }
      }
      
    }
    
  }
  
  static final class GroupHandler_SECTION implements GroupHandler {
  
    public void parse(State state, DxfReader dxf) {
      DxfUtil.advanceToGroupCode(2, dxf);
      state.currentSection = dxf.getCurrentElement().getStringValue();
      dxf.advance();
    }
    
  }
  
  static final class GroupHandler_ENDSEC implements GroupHandler {
    public void parse(State state, DxfReader dxf) {
      state.currentSection = null;
      dxf.advance();
    }
  }
  
  static final class GroupHandler_INSERT implements GroupHandler {

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.loader.dxf.SceneBuilder.GroupHandler#parse(ca.eandb.jmist.framework.loader.dxf.SceneBuilder.State, ca.eandb.jmist.framework.loader.dxf.DxfReader)
     */
    @Override
    public void parse(State state, DxfReader dxf) {
      
      DxfElement elem;
      double[] p = { 0.0, 0.0, 0.0 };
      double[] scale = { 1.0, 1.0, 1.0 };
      double[] ext = { 0.0, 0.0, 1.0 };
      int gc;
      
      String ref = null;
      double angle = 0.0;
      
      do {
        dxf.advance();
        elem = dxf.getCurrentElement();
        gc = elem.getGroupCode();
        
        switch (gc) {
        
        case 2:
          ref = elem.getStringValue();
          break;
        
        case 10:
        case 20:
        case 30:
          p[(gc - 10) / 10] = elem.getFloatValue();
          break;
          
        case 41:
        case 42:
        case 43:
          scale[gc - 41] = elem.getFloatValue();
          break;
          
        case 50:
          angle = elem.getFloatValue();
          break;
          
        case 210:
        case 220:
        case 230:
          ext[(gc - 210) / 10] = elem.getFloatValue();
          break;
          
        }
      } while (gc != 0);
      
      Block block = state.blocks.get(ref);
      if (block.geometry.getNumPrimitives() == 0) {
        return;
      }
//      double tol = 1.0;
//      if (!MathUtil.equal(angle, 0.0, tol) && !MathUtil.equal(angle, 180.0, tol) && !MathUtil.equal(angle, 360.0, tol)) {// && !MathUtil.equal(angle, 90.0, tol) && !MathUtil.equal(angle, 270.0, tol)) {
//        System.err.printf("Non-standard angle: %f degrees", angle);
//        System.err.println();
//        return;
//      }
      
      Point3 insertionPoint = new Point3(p[0], p[1], p[2]);
      Vector3 extrusionDir = new Vector3(ext[0], ext[1], ext[2]);
      
//      if (extrusionDir.minus(Vector3.K).squaredLength() > MathUtil.EPSILON) { System.err.printf("%f %f %f", extrusionDir.x(), extrusionDir.y(), extrusionDir.z()); System.err.println(); return; }
//      if (!MathUtil.areEqual(scale)) {
//        System.err.println("Non-uniform scaling");
//      }
      
      if (block.root == null) {
        int np = block.geometry.getNumPrimitives();
        if (np >= ACCEL_THRESHOLD) {
          block.root = new BoundingIntervalHierarchy(block.geometry); 
        } else {
          block.root = block.geometry;
        }
      }
      
      TransformableSceneElement e = new TransformableSceneElement(block.root);
      e.translate(block.base.vectorFromOrigin());
      e.stretch(scale[0], scale[1], scale[2]);      
      e.rotateZ(Math.toRadians(angle));
      
      e.translate(insertionPoint.vectorFromOrigin());
      Basis3 basis = DxfUtil.getBasisFromArbitraryAxis(extrusionDir);
      AffineMatrix3 T = AffineMatrix3.fromColumns(basis.u(), basis.v(), basis.w());
      e.transform(T);

      
      if (state.currentSection.equals("ENTITIES")) {
        state.root.addChild(block.root instanceof BoundingIntervalHierarchy ? new CollapseSceneElement(e) : e);
      }
      if (state.currentBlock != null) {
        state.currentBlock.geometry.addChild(block.root instanceof BoundingIntervalHierarchy ? new CollapseSceneElement(e) : e);
      }
    }
    
  }

  private static final Map<String, GroupHandler> handlers = new HashMap<String, GroupHandler>();
  {
    Class<?>[] classes = DxfSceneBuilder.class.getDeclaredClasses();
    for (Class<?> clazz : classes) {
      String name = clazz.getSimpleName();
      if (name.startsWith("GroupHandler_")) {
        String type = name.substring(13);
        try {
          GroupHandler handler = (GroupHandler) clazz.newInstance();
          handlers.put(type, handler);
        } catch (Exception e) {
          System.err.printf("Failed to instantiate %s", name);
          System.err.println();
          e.printStackTrace();
        }
      }
    }
  }
  
  private static final GroupHandler rootGroupHandler = new GroupHandler() {

    /* (non-Javadoc)
     * @see ca.eandb.jmist.framework.loader.dxf.SceneBuilder.GroupHandler#parse(ca.eandb.jmist.framework.loader.dxf.SceneBuilder.State, ca.eandb.jmist.framework.loader.dxf.DxfReader)
     */
    @Override
    public void parse(State state, DxfReader dxf) {
      String key = dxf.getCurrentElement().getStringValue();
      GroupHandler handler = handlers.get(key);
      if (handler != null) {
        handler.parse(state, dxf);
      } else {
        dxf.advance();
      }
    }
    
  };
    
  public SceneElement createScene(ColorModel cm, DxfReader dxf) {
    State state = new State();
    do {
      DxfUtil.advanceToGroupCode(0, dxf);
      if (dxf.getCurrentElement().getStringValue().equals("EOF")) {
        break;
      }
      rootGroupHandler.parse(state, dxf);
    } while (true);
    return new MaterialSceneElement(new LambertianMaterial(cm.getGray(0.5)), new BoundingIntervalHierarchy(state.root));
  }
  
  public static void main(String[] args) {
    String fn = "C:\\Users\\Brad\\Documents\\11-01-22 Sun Life Atria Q2.dxf";
    FileReader fr;
    try {
      fr = new FileReader(fn);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return;
    }
    DxfReader dxf = new AsciiDxfReader(fr);
    DxfSceneBuilder builder = new DxfSceneBuilder();
    ColorModel cm = RGBColorModel.getInstance();
    SceneElement elem = builder.createScene(cm, dxf);
    System.out.println(elem.toString());
  }

}
