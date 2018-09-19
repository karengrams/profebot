package ar.com.profebot.service;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.profebot.Models.MultipleChoiceStep;
import ar.com.profebot.activities.SolvePolynomialActivity;
import io.github.kexanie.library.MathView;

public class RVMultipleChoicePlynomialAdapter extends RecyclerView.Adapter<RVMultipleChoicePlynomialAdapter.MultipleChoiceViewHolder> {

    private List<MultipleChoiceStep> multipleChoiceSteps;
    private List<MultipleChoiceStep> currentMultipleChoiceSteps;
    private static List<MultipleChoiceViewHolder> multipleChoiceViewHolders = new ArrayList<>();

    public static class MultipleChoiceViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        MathView equationBase;
        MathView newEquationBase;
        ImageView expandCollapseIndicator;
        ImageView expandCollapseIndicatorColor;
        TextView summary;
        LinearLayout multipleChoiceResolutionStep;
        LinearLayout multipleChoiceSolvedResolutionStep;
        Button solveStep;
        Button nextStep;
        LinearLayout layoutToUse;
        RadioButton optionA;
        RadioButton optionB;
        RadioButton optionC;
        RadioButton correctOptionRadio;
        RadioButton incorrectOptionRadio;
        Integer chosenOption;
        Integer correctOption;
        Integer regularOption1;
        Integer regularOption2;
        String correctOptionJustification;
        String incorrectOptionJustification1;
        String incorrectOptionJustification2;
        List<MultipleChoiceStep> multipleChoiceSteps;
        List<MultipleChoiceStep> currentMultipleChoiceSteps;
        TextView numberStep;
        public Boolean isSolved = false;

        private void setUpSolveButton(){
            if(!solveStep.isEnabled()){
                solveStep.setEnabled(true);
                solveStep.setBackgroundResource(R.color.colorGreen);
                solveStep.setTextColor(Color.WHITE);
                solveStep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SolvePolynomialActivity.recyclerView.scrollToPosition(0);
                        isSolved = true;
                        multipleChoiceResolutionStep.setVisibility(View.GONE);
                        multipleChoiceSolvedResolutionStep.setVisibility(View.VISIBLE);
                        layoutToUse = multipleChoiceSolvedResolutionStep;

                        correctOptionRadio.setText(correctOptionJustification);
                        String summaryText;
                        if(chosenOption.equals(correctOption)){
                            incorrectOptionRadio.setVisibility(View.GONE);
                            expandCollapseIndicatorColor.setBackgroundResource(R.drawable.solved_right);
                            FactoringManager.factorizeBy(chosenOption);
                            String correctText = FactoringManager.getMessageOfRightOption(chosenOption);
                            String complementText = FactoringManager.getMessageOfRegularOptions(regularOption1, regularOption2);
                            correctOptionRadio.setText(correctText + " " + complementText);
                            summaryText = FactoringManager.getCaseNameFrom(chosenOption);
                        }else if(chosenOption.equals(regularOption1) || chosenOption.equals(regularOption2)){
                            incorrectOptionRadio.setVisibility(View.GONE);
                            expandCollapseIndicatorColor.setBackgroundResource(R.drawable.solved_right);
                            FactoringManager.factorizeBy(chosenOption);
                            String regularText = FactoringManager.getMessageOfRegularOptionChosen(chosenOption);
                            String complementText = chosenOption.equals(regularOption1)
                                    ? (regularOption2 == null ? "" : FactoringManager.getMessageOfRegularOptionNotChosen(regularOption2))
                                    : (regularOption1 == null ? "" : FactoringManager.getMessageOfRegularOptionNotChosen(regularOption1));
                            String correctText = FactoringManager.getMessageOfRightOptionNotChosen(correctOption);
                            correctOptionRadio.setText(regularText + " " + complementText + " " + correctText);
                            summaryText = FactoringManager.getCaseNameFrom(chosenOption);
                        }else{
                            expandCollapseIndicatorColor.setBackgroundResource(R.drawable.solved_wrong);
                            Map<Integer, String> incorrectOptions = new HashMap<>();
                            for(int i = 1 ; i <= 3 ; i++){
                                if(i != correctOption){
                                    if(incorrectOptions.isEmpty()){
                                        incorrectOptions.put(i, incorrectOptionJustification1);
                                    }else{
                                        incorrectOptions.put(i, incorrectOptionJustification2);
                                    }
                                }
                            }
                            incorrectOptionRadio.setVisibility(View.VISIBLE);
                            incorrectOptionRadio.setText(FactoringManager.getMessageOfWrongOptionChosen(chosenOption));

                            FactoringManager.factorizeBy(correctOption);
                            String regularText = (regularOption1 == null ? "" : FactoringManager.getMessageOfRegularOptionNotChosen(regularOption1));
                            regularText += (regularOption2 == null ? "" : FactoringManager.getMessageOfRegularOptionNotChosen(regularOption2));
                            String correctText = FactoringManager.getMessageOfRightOptionNotChosen(correctOption);
                            correctOptionRadio.setText(correctText + " " + regularText);
                            summaryText = FactoringManager.getCaseNameFrom(correctOption);
                        }
                        expandCollapseIndicatorColor.setVisibility(View.VISIBLE);

                        summary.setText(summaryText);
                        FactoringManager.setFactors();
                        newEquationBase.setText("$$" + FactoringManager.getEquationAfterFactorizing() + "$$");
                        if(!FactoringManager.end){
                            multipleChoiceSteps.add(FactoringManager.nextStep());
                            MultipleChoiceStep currentMultipleChoiceStep = multipleChoiceSteps.get(currentMultipleChoiceSteps.size()-1);
                            currentMultipleChoiceStep.setSolved(true);
                            setUpNextStepButton();
                        }else{
                            nextStep.setVisibility(View.GONE);
                            FactoringManager.enableSummary(false);
                        }

                        multipleChoiceSteps.get(currentMultipleChoiceSteps.size()-1).setSolved(true);
                        SolvePolynomialActivity.recyclerView.scrollToPosition(currentMultipleChoiceSteps.size() - 1);
                    }
                });
            }
        }

        private String getAsInfix(String equation){
            return equation
                    .replace("\\(", "")
                    .replace("\\)", "");
        }

        private void setUpNextStepButton(){
            nextStep.setEnabled(true);
            nextStep.setBackgroundResource(R.color.colorGreen);
            nextStep.setTextColor(Color.WHITE);
            nextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SolvePolynomialActivity.recyclerView.scrollToPosition(0);
                    nextStep.setVisibility(View.GONE);
                    layoutToUse.setVisibility(View.GONE);
                    expandCollapseIndicator.setScaleY(1f);
                    currentMultipleChoiceSteps.add(multipleChoiceSteps.get(currentMultipleChoiceSteps.size()));
                }
            });
        }

        MultipleChoiceViewHolder(View itemView, List<MultipleChoiceStep> currentMultipleChoiceSteps) {
            super(itemView);
            card = itemView.findViewById(R.id.step_id);
            numberStep = itemView.findViewById(R.id.number_step_id);
            equationBase = itemView.findViewById(R.id.equation_base_id);
            newEquationBase = itemView.findViewById(R.id.new_equation_base_id);
            expandCollapseIndicator = itemView.findViewById(R.id.expand_collapse_indicator_id);
            expandCollapseIndicatorColor = itemView.findViewById(R.id.expand_collapse_indicator_color_id);
            summary = itemView.findViewById(R.id.summary_id);

            multipleChoiceResolutionStep = itemView.findViewById(R.id.multiple_choice_section_id);
            multipleChoiceSolvedResolutionStep = itemView.findViewById(R.id.multiple_choice_solved_section_id);
            multipleChoiceResolutionStep.setVisibility(View.GONE);
            multipleChoiceSolvedResolutionStep.setVisibility(View.GONE);

            correctOptionRadio = itemView.findViewById(R.id.option_correct_id);
            incorrectOptionRadio = itemView.findViewById(R.id.option_incorrect_id);

            if(!isSolved){
                // When new card is added to RV. It indicates if has to be initialized as expanded or collapsed (default expanded)
                expandCollapseIndicator.setScaleY(-1f);
                multipleChoiceResolutionStep.setVisibility(View.VISIBLE);

                layoutToUse = multipleChoiceResolutionStep;

                solveStep = itemView.findViewById(R.id.solve_step_id);
                solveStep.setEnabled(false);
                nextStep = itemView.findViewById(R.id.next_step_id);

                MultipleChoiceViewHolder viewHolder = this;

                optionA = itemView.findViewById(R.id.option_a_id);
                optionA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.setUpSolveButton();
                        chosenOption = 1;
                    }
                });

                optionB = itemView.findViewById(R.id.option_b_id);
                optionB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.setUpSolveButton();
                        chosenOption = 2;
                    }
                });

                optionC = itemView.findViewById(R.id.option_c_id);
                optionC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.setUpSolveButton();
                        chosenOption = 3;
                    }
                });
            }else{
                layoutToUse = multipleChoiceSolvedResolutionStep;
            }

            expandCollapseIndicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean shouldExpand = layoutToUse.getVisibility() == View.GONE;
                    if(shouldExpand){
                        expandCollapseIndicator.setScaleY(-1f);
                        layoutToUse.setVisibility(View.VISIBLE);
                    }else{
                        layoutToUse.setVisibility(View.GONE);
                        expandCollapseIndicator.setScaleY(1f);
                    }
                }
            });
        }
    }

    public RVMultipleChoicePlynomialAdapter(MultipleChoiceStep firstStep, List<MultipleChoiceStep> multipleChoiceSteps){
        this.multipleChoiceSteps = multipleChoiceSteps;
        this.currentMultipleChoiceSteps = new ArrayList<>();
        if(firstStep != null){
            this.currentMultipleChoiceSteps.add(firstStep);
        }
    }

    @Override
    public int getItemCount() {
        return currentMultipleChoiceSteps.size();
    }

    @Override
    public MultipleChoiceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.resolution_step, viewGroup, false);
        MultipleChoiceViewHolder multipleChoiceViewHolder = new MultipleChoiceViewHolder(v, this.currentMultipleChoiceSteps);
        multipleChoiceViewHolders.add(multipleChoiceViewHolder);
        return multipleChoiceViewHolder;
    }

    @Override
    public void onBindViewHolder(MultipleChoiceViewHolder multipleChoiceViewHolder, int position) {
        multipleChoiceViewHolder.equationBase.setEngine(MathView.Engine.MATHJAX);
        multipleChoiceViewHolder.equationBase.config("MathJax.Hub.Config({\n"+
                "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                "         SVG: { linebreaks: { automatic: true } }\n"+
                "});");
        multipleChoiceViewHolder.equationBase.setText("\\(" + currentMultipleChoiceSteps.get(position).getEquationBase() + "\\)");

        multipleChoiceViewHolder.newEquationBase.setEngine(MathView.Engine.MATHJAX);
        multipleChoiceViewHolder.newEquationBase.config("MathJax.Hub.Config({\n"+
                "  CommonHTML: { linebreaks: { automatic: true } },\n"+
                "  \"HTML-CSS\": { linebreaks: { automatic: true } },\n"+
                "         SVG: { linebreaks: { automatic: true } }\n"+
                "});");

        if(currentMultipleChoiceSteps.get(position).getSolved()){
            multipleChoiceViewHolder.summary.setText(currentMultipleChoiceSteps.get(position).getSummary());
        }
        multipleChoiceViewHolder.optionA.setText(currentMultipleChoiceSteps.get(position).getOptionA());
        multipleChoiceViewHolder.optionB.setText(currentMultipleChoiceSteps.get(position).getOptionB());
        multipleChoiceViewHolder.optionC.setText(currentMultipleChoiceSteps.get(position).getOptionC());
        multipleChoiceViewHolder.correctOption = currentMultipleChoiceSteps.get(position).getCorrectOption();
        multipleChoiceViewHolder.regularOption1 = currentMultipleChoiceSteps.get(position).getRegularOption1();
        multipleChoiceViewHolder.regularOption2 = currentMultipleChoiceSteps.get(position).getRegularOption2();
        multipleChoiceViewHolder.correctOptionJustification = currentMultipleChoiceSteps.get(position).getCorrectOptionJustification();
        multipleChoiceViewHolder.incorrectOptionJustification1 = currentMultipleChoiceSteps.get(position).getIncorrectOptionJustification1();
        multipleChoiceViewHolder.incorrectOptionJustification2 = currentMultipleChoiceSteps.get(position).getIncorrectOptionJustification2();
        multipleChoiceViewHolder.currentMultipleChoiceSteps = currentMultipleChoiceSteps;
        multipleChoiceViewHolder.multipleChoiceSteps = multipleChoiceSteps;
        multipleChoiceViewHolder.numberStep.setText((position+1) + ")");

        if(multipleChoiceSteps.get(position).getSolved()){
            multipleChoiceViewHolder.multipleChoiceSolvedResolutionStep.setVisibility(View.VISIBLE);
            multipleChoiceViewHolder.multipleChoiceResolutionStep.setVisibility(View.INVISIBLE);
            multipleChoiceViewHolder.layoutToUse = multipleChoiceViewHolder.multipleChoiceSolvedResolutionStep;
        }else{
            multipleChoiceViewHolder.multipleChoiceResolutionStep.setVisibility(View.VISIBLE);
            multipleChoiceViewHolder.multipleChoiceSolvedResolutionStep.setVisibility(View.INVISIBLE);
            multipleChoiceViewHolder.layoutToUse = multipleChoiceViewHolder.multipleChoiceResolutionStep;

            multipleChoiceViewHolder.expandCollapseIndicatorColor.setVisibility(View.INVISIBLE);

            multipleChoiceViewHolder.isSolved = false;

            multipleChoiceViewHolder.summary.setText("Pendiente");

            // When new card is added to RV. It indicates if has to be initialized as expanded or collapsed (default expanded)
            multipleChoiceViewHolder.expandCollapseIndicator.setScaleY(-1f);

            multipleChoiceViewHolder.solveStep.setEnabled(false);
            if(currentMultipleChoiceSteps.size() < multipleChoiceSteps.size()){
                multipleChoiceViewHolder.nextStep.setVisibility(View.VISIBLE);
            }

            multipleChoiceViewHolder.chosenOption = null;

            multipleChoiceViewHolder.optionA = multipleChoiceViewHolder.itemView.findViewById(R.id.option_a_id);
            multipleChoiceViewHolder.optionA.setChecked(false);
            multipleChoiceViewHolder.optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    multipleChoiceViewHolder.setUpSolveButton();
                    multipleChoiceViewHolder.chosenOption = 1;
                }
            });

            multipleChoiceViewHolder.optionB = multipleChoiceViewHolder.itemView.findViewById(R.id.option_b_id);
            multipleChoiceViewHolder.optionB.setChecked(false);
            multipleChoiceViewHolder.optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    multipleChoiceViewHolder.setUpSolveButton();
                    multipleChoiceViewHolder.chosenOption = 2;
                }
            });

            multipleChoiceViewHolder.optionC = multipleChoiceViewHolder.itemView.findViewById(R.id.option_c_id);
            multipleChoiceViewHolder.optionC.setChecked(false);
            multipleChoiceViewHolder.optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    multipleChoiceViewHolder.setUpSolveButton();
                    multipleChoiceViewHolder.chosenOption = 3;
                }
            });

            multipleChoiceViewHolder.expandCollapseIndicator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean shouldExpand = multipleChoiceViewHolder.layoutToUse.getVisibility() == View.GONE;
                    if(shouldExpand){
                        multipleChoiceViewHolder.expandCollapseIndicator.setScaleY(-1f);
                        multipleChoiceViewHolder.layoutToUse.setVisibility(View.VISIBLE);
                    }else{
                        multipleChoiceViewHolder.layoutToUse.setVisibility(View.GONE);
                        multipleChoiceViewHolder.expandCollapseIndicator.setScaleY(1f);
                    }
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
