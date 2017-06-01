package com.acercraft.AcerGun;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class weaponReader
{
  public Gun ret;
  public boolean loaded = true;
  public main main;
  public File file;
  public String type;

  public weaponReader(main main, File file, String type)
  {
    this.main = main;
    this.file = file;
    this.type = type;

    if (type.equals("projectile"))
      this.ret = new Throwable(main);
    else {
      this.ret = new Gun(main);
    }

    load();
  }

  public void computeData(String str) {
    try {
      if (str.indexOf("=") > 0) {
        String str2 = str.substring(0, str.indexOf("="));
        if (str2.equalsIgnoreCase("gunMat")) {
          int value = Integer.parseInt(str.substring(str.indexOf("=") + 1));
          this.ret.gunMat = value;
        }
        if (str2.equalsIgnoreCase("ammoMat")) {
          int value = Integer.parseInt(str.substring(str.indexOf("=") + 1));
          this.ret.ammoMat = value;
        }
        if (str2.equalsIgnoreCase("ammoNeeded")) {
          int value = Integer.parseInt(str.substring(str.indexOf("=") + 1));
          this.ret.ammoNeeded = value;
        }
        if (str2.equalsIgnoreCase("reloadTime")) {
          int value = Integer.parseInt(str.substring(str.indexOf("=") + 1));
          this.ret.reloadTime = value;
        }
        if (str2.equalsIgnoreCase("roundsPerBurst")) {
          int value = Integer.parseInt(str.substring(str.indexOf("=") + 1));
          this.ret.roundsPerBurst = value;
        }
        if (str2.equalsIgnoreCase("bulletsPerClick")) {
          int value = Integer.parseInt(str.substring(str.indexOf("=") + 1));
          this.ret.bulletsPerClick = value;
        }
        if (str2.equalsIgnoreCase("bulletSpeed")) {
          double value = Double.parseDouble(str.substring(str.indexOf("=") + 1));
          this.ret.bulletSpeed = value;
        }
        if (str2.equalsIgnoreCase("accuracy")) {
          double value = Double.parseDouble(str.substring(str.indexOf("=") + 1));
          this.ret.accuracy = value;
        }
        if (str2.equalsIgnoreCase("maxDistance")) {
          int value = Integer.parseInt(str.substring(str.indexOf("=") + 1));
          this.ret.maxDistance = value;
        }
        if (str2.equalsIgnoreCase("damage")) {
          int value = Integer.parseInt(str.substring(str.indexOf("=") + 1));
          this.ret.damage = value;
        }
        if (str2.equalsIgnoreCase("armorPenetration")) {
          int value = Integer.parseInt(str.substring(str.indexOf("=") + 1));
          this.ret.armorPenetration = value;
        }
        if (str2.equalsIgnoreCase("fireradius")) {
          int value = Integer.parseInt(str.substring(str.indexOf("=") + 1));
          this.ret.fireradius = value;
        }
        if (str2.equalsIgnoreCase("needsPermission")) {
          boolean value = Boolean.parseBoolean(str.substring(str.indexOf("=") + 1));
          this.ret.needsPermission = value;
        }
        if (str2.equalsIgnoreCase("smokeTrail")) {
          boolean value = Boolean.parseBoolean(str.substring(str.indexOf("=") + 1));
          this.ret.smokeTrail = value;
        }
        if (str2.equalsIgnoreCase("explode")) {
          boolean value = Boolean.parseBoolean(str.substring(str.indexOf("=") + 1));
          this.ret.explode = value;
        }
        if (str2.equalsIgnoreCase("cockable")) {
          boolean value = Boolean.parseBoolean(str.substring(str.indexOf("=") + 1));
          this.ret.cockable = value;
        }
        if (str2.equalsIgnoreCase("boltaction")) {
          boolean value = Boolean.parseBoolean(str.substring(str.indexOf("=") + 1));
          this.ret.boltaction = value;
        }
        if (str2.equalsIgnoreCase("firebullet")) {
          boolean value = Boolean.parseBoolean(str.substring(str.indexOf("=") + 1));
          this.ret.firebullet = value;
        }
        if (str2.equalsIgnoreCase("tinyexplode")) {
          boolean value = Boolean.parseBoolean(str.substring(str.indexOf("=") + 1));
          this.ret.tinyexplode = value;
        }
        if (str2.equalsIgnoreCase("explodeBlocks")) {
          boolean value = Boolean.parseBoolean(str.substring(str.indexOf("=") + 1));
          this.ret.explodeBlocks = value;
        }
        if (str2.equalsIgnoreCase("rightclickfire")) {
          boolean value = Boolean.parseBoolean(str.substring(str.indexOf("=") + 1));
          this.ret.rightclickfire = value;
        }
      }
    } catch (Exception e) {
      this.loaded = false;
      System.out.println("[AcerGun] error loading gun!");
    }
  }

  public void load() {
    ArrayList<String> file = new ArrayList<String>();
    try {
      FileInputStream fstream = new FileInputStream(this.file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      while ((strLine = br.readLine()) != null)
      {
    
        file.add(strLine);
      }
      br.close();
      in.close();
      fstream.close();
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }

    for (int i = 0; i < file.size(); i++)
      computeData((String)file.get(i));
  }
}