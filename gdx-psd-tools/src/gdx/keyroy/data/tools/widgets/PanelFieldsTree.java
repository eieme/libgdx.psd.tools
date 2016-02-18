package gdx.keyroy.data.tools.widgets;

import gdx.keyroy.data.tools.models.ClassElement;
import gdx.keyroy.data.tools.models.ImagePath;
import gdx.keyroy.psd.tools.util.MessageListener;
import gdx.keyroy.psd.tools.util.Messager;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

// ���������
@SuppressWarnings("serial")
public class PanelFieldsTree extends JPanel {
	private JScrollPane scrollPane;

	/**
	 * Create the panel.
	 */
	public PanelFieldsTree() {
		setLayout(new BorderLayout(0, 0));
		scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		initMessager();
	}

	private final void initMessager() {
		Messager.register(ImagePath.class, new MessageListener<ImagePath>() {
			@Override
			public void onMessage(ImagePath t, Object[] params) {
				if (t.isAtlas()) {
					scrollPane.setViewportView(new ImageAtlasList(t));
				} else {
					scrollPane.setViewportView(null);
				}
			}
		});

		Messager.register(ClassElement.class, new MessageListener<ClassElement>() {
			@Override
			public void onMessage(ClassElement t, Object[] params) {
				scrollPane.setViewportView(new ClassElementFieldTree(t));
			}
		});
	}

}
