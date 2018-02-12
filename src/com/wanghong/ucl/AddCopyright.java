package com.wanghong.ucl;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.Objects;

public class AddCopyright extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        DefaultComboBoxModel<License> comboBoxModel = new DefaultComboBoxModel<>(
                LicenseLoader.loadLicenses().toArray(new License[0]));
        JComboBox<License> licenseJComboBox = new ComboBox<>(comboBoxModel);

        JButton okButton = new JButton("OK");
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.PAGE_AXIS));
        jPanel.add(licenseJComboBox);
        jPanel.add(okButton);

        JBPopup jbPopup = JBPopupFactory.getInstance().createComponentPopupBuilder(jPanel, jPanel)
                .setTitle("Select which license to apply")
                .createPopup();
        jbPopup.showInBestPositionFor(e.getRequiredData(DataKeys.EDITOR));

        okButton.addActionListener(e0 -> {
            jbPopup.cancel();
            applyLicense(e, (License) licenseJComboBox.getSelectedItem());
        });

        licenseJComboBox.addItemListener(e1 -> {
            if (e1.getStateChange() == ItemEvent.SELECTED) {
                jbPopup.cancel();
                applyLicense(e, (License) licenseJComboBox.getSelectedItem());
            }
        });
    }

    private void applyLicense(AnActionEvent event, License license) {
        Editor editor = event.getData(DataKeys.EDITOR);
        if (editor != null) {
            VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
            Language language = LanguageUtil.getFileLanguage(virtualFile);
            FileType fileType = LanguageUtil.getLanguageFileType(language);
            if (fileType != null) {
                license.setInsertIntoFileType(fileType.getDefaultExtension());
                license.setProjectName(Objects.requireNonNull(event.getProject()).getName());

                WriteCommandAction.runWriteCommandAction(event.getProject(),
                        () -> editor.getDocument().insertString(0, license.getLicense()));
            }
        }
    }
}
