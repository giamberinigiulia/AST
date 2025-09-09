package com.giulia.giamberini.tennis.view.swing;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class TennisManagementViewSwingTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private TennisManagementViewSwing view;

	@Override
	protected void onSetUp() throws Exception {
		GuiActionRunner.execute(() -> {
			view = new TennisManagementViewSwing();
			return view;
		});
		window = new FrameFixture(robot(), view);
		window.show();
	}

	@Test @GUITest
	public void testInitialConfiguration() {
		window.requireTitle("Tennis Matches Management");
		window.label(JLabelMatcher.withText("ID"));
		window.textBox("idTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Name"));
		window.textBox("nameTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Surname"));
		window.textBox("surnameTextBox").requireEnabled();
		window.button(JButtonMatcher.withText("Add player")).requireDisabled();
		window.list("playersList").requireEnabled();
		window.button(JButtonMatcher.withText("Delete player")).requireDisabled();
		window.label("errorPlayerLbl").requireText(" ");
		window.label(JLabelMatcher.withText("Winner"));
		window.comboBox("winnerComboBox").requireDisabled();
		window.label(JLabelMatcher.withText("Loser"));
		window.comboBox("loserComboBox").requireDisabled();
		window.label(JLabelMatcher.withText("Date"));
		window.textBox("dateOfTheMatchTextBox").requireEnabled();
		window.button(JButtonMatcher.withText("Add match")).requireDisabled();
		window.list("matchesList").requireEnabled();
		window.button(JButtonMatcher.withText("Delete match")).requireDisabled();
		window.label("errorMatchLbl").requireText(" ");
	}

}
