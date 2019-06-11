import { QuizItem } from './quiz-item';
export class ReviewItem {
  id: number;
  content: string;
  hidden: boolean;
  score: number = 0;

  constructor(public quizItem: QuizItem) {

  }
}
