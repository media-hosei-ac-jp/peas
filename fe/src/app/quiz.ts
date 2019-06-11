import { QuizItem } from './quiz-item';
export class Quiz {
  id: number;
  created;
  modified;
  resultPublished: boolean = true;
  deleted: boolean = false;
  title: string;
  description: string;
  items: QuizItem[] = [];
  autoAddMember: boolean = true;
  anonymous: boolean;
  guestReview: boolean;
}
