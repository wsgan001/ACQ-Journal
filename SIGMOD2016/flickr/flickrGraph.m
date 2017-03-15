set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [1.23770884	8.135571513	24.54722141	48.08920839	155.0203639];
y2 = [1.170696127	7.673314443	22.35293529	41.46024307	128.5573833];
y3 = [0.21992646	1.942132173	7.219602537	16.65207827	44.60634139];

p1= plot(x, y1, '-kv');
hold on;
p2 = plot(x, y2, '-ks');
hold on;
p3 = plot(x, y3, '-kd');

xlabel('percentage of vertices');
ylabel('time (ms)');

axis([0.5 5.5 0.0 200]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'20%','40%','60%','80%','100%'});
leg=legend('Inc-S','Inc-T','Dec', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);