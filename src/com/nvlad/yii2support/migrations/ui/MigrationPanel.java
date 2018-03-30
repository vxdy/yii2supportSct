package com.nvlad.yii2support.migrations.ui;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckedTreeNode;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;
import com.nvlad.yii2support.migrations.MigrationManager;
import com.nvlad.yii2support.migrations.MigrationsMouseListener;
import com.nvlad.yii2support.migrations.actions.OrderAscAction;
import com.nvlad.yii2support.migrations.actions.RefreshAction;
import com.nvlad.yii2support.migrations.entities.Migration;
import com.nvlad.yii2support.migrations.util.MigrationUtil;
import com.nvlad.yii2support.utils.Yii2SupportSettings;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import java.util.Collection;
import java.util.Map;

public class MigrationPanel extends SimpleToolWindowPanel {
    private final Project myProject;
    private CheckboxTree myTree;

    public MigrationPanel(Project project) {
        super(false);

        myProject = project;

        initContent();
        initToolBar();
    }

    private void initContent() {
        MigrationTreeCellRenderer renderer = new MigrationTreeCellRenderer();
        CheckedTreeNode myRootNode = new CheckedTreeNode("");
        myRootNode.add(new DefaultMutableTreeNode("Init"));

        myTree = new CheckboxTree(renderer, myRootNode);
        myTree.addMouseListener(new MigrationsMouseListener());
        myTree.getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);

        JBScrollPane scrollPane = new JBScrollPane(myTree);
        UIUtil.removeScrollBorder(scrollPane);
        setContent(scrollPane);

        Map<String, Collection<Migration>> migrationTree = MigrationManager.getInstance(myProject).getMigrations();
        boolean newestFirst = Yii2SupportSettings.getInstance(myProject).newestFirst;
        MigrationUtil.updateTree(myTree, migrationTree, true, newestFirst);
    }


    private void initToolBar() {
        ActionToolbar toolbar = createToolbar();
        setToolbar(toolbar.getComponent());
    }

    private ActionToolbar createToolbar() {
        DefaultActionGroup group = new DefaultActionGroup();
        try {
            group.add(createAction(RefreshAction.class, myTree));
            group.add(new Separator());
            group.add(createAction(OrderAscAction.class, myTree));
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        return ActionManager.getInstance().createActionToolbar("Migrations", group, false);
    }

    private <T extends AnActionButton> T createAction(Class<T> clazz, JTree tree) throws IllegalAccessException, InstantiationException {
        T action = clazz.newInstance();
        action.setContextComponent(tree);
        return action;
    }
}
