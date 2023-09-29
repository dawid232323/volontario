import { AngularEditorConfig } from '@kolkov/angular-editor';

interface AngularEditorConfigProviderIf {
  config: AngularEditorConfig;
}

export const defaultAngularEditorConfig: AngularEditorConfig = {
  editable: true,
  toolbarHiddenButtons: [
    [],
    ['insertImage', 'insertVideo', 'backgroundColorPicker', 'toggleEditorMode'],
  ],
};

export class DefaultAngularEditorConfigProvider
  implements AngularEditorConfigProviderIf
{
  private static _config: AngularEditorConfig = {
    editable: true,
    toolbarHiddenButtons: [
      ['fontName'],
      [
        'insertImage',
        'insertVideo',
        'backgroundColorPicker',
        'toggleEditorMode',
      ],
    ],
  };

  public get config(): AngularEditorConfig {
    return DefaultAngularEditorConfigProvider._config;
  }
}

export class DisabledAngularEditorConfigProvider extends DefaultAngularEditorConfigProvider {
  public override get config(): AngularEditorConfig {
    return { ...super.config, editable: false };
  }
}
