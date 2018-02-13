package com.nvlad.yii2support.utils;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oleg on 2017-09-06.
 */
@State(name = "Yii2 support", storages = @Storage("yii2settings.xml"))
public class Yii2SupportSettings implements PersistentStateComponent<Yii2SupportSettings> {

    public String tablePrefix = "";
    public boolean insertWithTablePrefix = false;
    public Map<String, String> viewPathMap;

    public Yii2SupportSettings() {
        viewPathMap = new HashMap<>();
        viewPathMap.put("@app/themes/*/modules", "@app/modules");
        viewPathMap.put("@app/themes/*/widgets", "@app/widgets");
        viewPathMap.put("@app/themes/*", "@app/views");
    }

    @Nullable
    @Override
    public Yii2SupportSettings getState() {
        return this;
    }

    @Override
    public void loadState(Yii2SupportSettings applicationService) {
        XmlSerializerUtil.copyBean(applicationService, this);
    }

    public static Yii2SupportSettings getInstance(Project project) {
        return ServiceManager.getService(project, Yii2SupportSettings.class);
    }

}